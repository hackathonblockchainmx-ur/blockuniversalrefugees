/**
 * 
 */
package blockchain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import blockchain.rest.server.Main;
import blockchain.rest.server.models.Document;
import blockchain.rest.server.models.Wallet;
import io.ipfs.api.IPFS;
import io.ipfs.multihash.Multihash;

/**
 * @author gerardo
 *
 */
public class BlockchainFacade {
	private static final Logger LOGGER = Logger.getLogger(BlockchainFacade.class.getName());
	public static final String BLOCKCHAIN_NETWORK_URL = "http://" + Main.IP_ADDRESS + ":7100";/* 8545 */
	public static final BigInteger NOVAHEMJO_GAS_PRICE = BigInteger.valueOf(27000/* 21000 */);
	public static final BigInteger NOVAHEMJO_GAS_LIMIT = BigInteger.valueOf(3000000);
	public static final BigInteger NOVAHEMJO_ASSIGNED_ETHER_NEW_WALLET = new BigInteger("7000000000000000000");// 7
																												// ETHER
																												// in
																												// WEIs
	private static final String NOVAHEMJO_COINBASE_JSON_FILE_CLOUD = "/novahemjo-cloud-coinbase-account.json";
	private static final String NOVAHEMJO_COMMON_WALLET_PASSWORD = "novahemjo";
	private static String WALLETS_PATH;
	private Web3j web3j;
	private Admin admin;
	private ObjectMapper mapper;
	private static volatile BlockchainFacade instance;
	private static Object mutex = new Object();
	private List<String> newCreatedWallets;

	private BlockchainFacade() {
		try {
			String path = BlockchainFacade.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			if (path.endsWith(".jar")) {
				path = path.substring(0, path.lastIndexOf("/") + 1);
			}
			WALLETS_PATH = URLDecoder.decode(path, "UTF-8");
			LOGGER.log(Level.INFO, WALLETS_PATH);
			// connect to the blockchain...
			admin = Admin.build(new HttpService(BLOCKCHAIN_NETWORK_URL));
			web3j = Web3j.build(new HttpService(BLOCKCHAIN_NETWORK_URL));
			LOGGER.log(Level.INFO, "Connected to " + BLOCKCHAIN_NETWORK_URL);
			newCreatedWallets = new ArrayList<>();
			web3j.transactionObservable().subscribe(tx -> {
				final String fromAddress = tx.getFrom();
				final String toAddress = tx.getTo();
				final BigInteger value = tx.getValue();
				LOGGER.log(Level.INFO,
						"tx - from address " + fromAddress + " to address " + toAddress + " value = " + value);
				final List<String> matches = newCreatedWallets.stream().filter(it -> it.equals(toAddress))
						.collect(Collectors.toList());
				if (matches.size() > 0) {
					try {
						LOGGER.log(Level.INFO,
								"found new tx with value " + value + " for new wallet with address " + toAddress);
						// check that we have enough ether to execute transactions before calling the
						// contract...
						final EthGetBalance ethGetBalance = web3j
								.ethGetBalance(toAddress, DefaultBlockParameterName.LATEST).send();
						final BigInteger balanceInWei = ethGetBalance.getBalance();
						LOGGER.log(Level.INFO, "balance of new wallet " + toAddress + " in ethers "
								+ Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER));
						if (balanceInWei.compareTo(BigInteger.ZERO) == 1) {
							// once we have the ether, call the new wallet user to the contract to be
							// registered...
							LOGGER.log(Level.INFO, "new wallet have positive ether balance.");
						} else {
							LOGGER.log(Level.INFO, "new wallet have no ether balance.");
						}
					} catch (Exception e) {
						LOGGER.log(Level.INFO, e.getMessage(), e);
					}
					matches.remove(tx.getTo());
				}
			});
			mapper = new ObjectMapper();
		} catch (Exception e) {
			LOGGER.log(Level.INFO, e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	// TODO: listen for events of the contract also...

	/**
	 * 
	 * @return
	 */
	public static BlockchainFacade getInstance() {
		BlockchainFacade result = instance;
		if (result == null) {
			synchronized (mutex) {
				result = instance;
				if (result == null)
					instance = result = new BlockchainFacade();
			}
		}
		return result;
	}

	//
	private void sendEtherToNewWalletFromCoinbaseWallet(final String walletAddress, final BigInteger etherAmount)
			throws Exception {
		String coinbaseAccount = Main.COINBASE_ACCOUNT_CLOUD;
		String coinbasePassword = Main.COINBASE_PASSWORD_CLOUD;
		String coinbaseJSONFile = NOVAHEMJO_COINBASE_JSON_FILE_CLOUD;
		PersonalUnlockAccount personalUnlockAccount = null;
		try {
			// this is mostly used when is in a background mode the geth node...
			personalUnlockAccount = admin.personalUnlockAccount(coinbaseAccount, coinbasePassword).send();
		} catch (Exception e) {
			LOGGER.log(Level.INFO,
					"Can't unlock coinbase account using personal api to send ethers, using coinbase wallet.");
			// if cannot send ether from personal admin, send via wallet json coinbase
			// directly...this also happens when we are in geth console mode...
			final File coinbaseWalletJSONFile = getResourceAsFile(coinbaseJSONFile);
			final Credentials credentials = WalletUtils.loadCredentials(coinbasePassword, coinbaseWalletJSONFile);
			final EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(coinbaseAccount, DefaultBlockParameterName.LATEST).send();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			LOGGER.log(Level.INFO, "nonce for tx " + nonce);
			// hack for pending txs...send new tx nonce...alternative is to replace new gas
			// nonce = nonce.add(BigInteger.ONE);
			// continue
			final RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, NOVAHEMJO_GAS_PRICE,
					NOVAHEMJO_GAS_LIMIT, walletAddress, etherAmount/* in wei */);
			final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			final String hexValue = Numeric.toHexString(signedMessage);
			final EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
			LOGGER.log(Level.INFO, "ether sent tx is " + ((!ethSendTransaction.hasError()) ? "ok" : "not ok"));
			if (ethSendTransaction.hasError()) {
				LOGGER.log(Level.SEVERE, "failed to send ether - " + ethSendTransaction.getError().getMessage());
				throw new Exception(ethSendTransaction.getError().getMessage());
			}
			LOGGER.log(Level.INFO, "ether sent tx - " + ethSendTransaction.getTransactionHash());
			LOGGER.log(Level.INFO, "ether sent was - " + Convert.fromWei(etherAmount.toString(), Convert.Unit.ETHER));
			return;
		}
		if (personalUnlockAccount != null && personalUnlockAccount.accountUnlocked()) {
			LOGGER.log(Level.INFO, "coinbase account unlocked using personal api.");
			// send ether
			BigInteger nonce = admin.ethGetTransactionCount(coinbaseAccount, DefaultBlockParameterName.LATEST).send()
					.getTransactionCount();
			LOGGER.log(Level.INFO, "nonce for tx " + nonce);
			// hack for pending txs...send new tx nonce...alternate replace with new gas
			// nonce = nonce.add(BigInteger.ONE);
			// continue
			final Transaction transaction = Transaction.createEtherTransaction(coinbaseAccount, nonce,
					NOVAHEMJO_GAS_PRICE, NOVAHEMJO_GAS_LIMIT, walletAddress, etherAmount/* in wei */);
			final EthSendTransaction ethSendTransaction = admin.ethSendTransaction(transaction).send();
			LOGGER.log(Level.INFO, "ether sent tx is " + ((!ethSendTransaction.hasError()) ? "ok" : "not ok"));
			if (ethSendTransaction.hasError()) {
				LOGGER.log(Level.SEVERE, "failed to send ether - " + ethSendTransaction.getError().getMessage());
				throw new Exception(ethSendTransaction.getError().getMessage());
			}
			LOGGER.log(Level.INFO, "ether sent tx - " + ethSendTransaction.getTransactionHash());
			LOGGER.log(Level.INFO, "ether sent was - " + Convert.fromWei(etherAmount.toString(), Convert.Unit.ETHER));
			return;
		} else
			throw new Exception("Can't unlock coinbase account, contact admin.");
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Wallet createWalletForRefuge() throws Exception {
		final File walletsDirectory = new File(WALLETS_PATH);
		LOGGER.log(Level.INFO, walletsDirectory.getAbsolutePath());
		final String newWalletFileName = WalletUtils.generateFullNewWalletFile(NOVAHEMJO_COMMON_WALLET_PASSWORD,
				walletsDirectory);
		LOGGER.log(Level.INFO, "new wallet file name - " + newWalletFileName);
		final String newWalletAddress = "0x" + newWalletFileName.substring(newWalletFileName.lastIndexOf("--") + 2,
				newWalletFileName.lastIndexOf('.'));
		LOGGER.log(Level.INFO, "new wallet address - " + newWalletAddress);
		final File newWalletFile = new File(walletsDirectory.getAbsolutePath() + File.separator + newWalletFileName);
		LOGGER.log(Level.INFO, "new wallet file at path - " + newWalletFile.getAbsolutePath());
		if (newWalletFile.exists() && newWalletFile.canWrite()) {
			final File renamedNewWalletFile = new File(
					walletsDirectory.getAbsolutePath() + File.separator + newWalletAddress + ".json");
			final boolean renamed = newWalletFile.renameTo(renamedNewWalletFile);
			LOGGER.log(Level.INFO, "new wallet file " + ((renamed) ? "renamed" : "not renamed") + " at path "
					+ renamedNewWalletFile.getAbsolutePath());
			// TODO: due to the bug of web3j that includes two copies of the kdf:scrypt
			// reference...we need to remove the references and rewrite the file with only
			// one...
			JsonNode jsonNode = mapper.readTree(renamedNewWalletFile);
			((ObjectNode) jsonNode.path("crypto")).remove("kdf");// removes all references...
			((ObjectNode) jsonNode.path("crypto")).put("kdf", "scrypt");// appends only one...
			FileChannel.open(Paths.get(renamedNewWalletFile.getAbsolutePath()), StandardOpenOption.WRITE).truncate(0)
					.close();
			mapper.writeValue(renamedNewWalletFile, jsonNode);
			// add to the list in order to check tx's
			newCreatedWallets.add(newWalletAddress);
			// in a background thread finish sending ether to the new account...
			CompletableFuture.supplyAsync(() -> {
				try {
					// give some ether for the transactions of the new client wallet...
					sendEtherToNewWalletFromCoinbaseWallet(newWalletAddress, NOVAHEMJO_ASSIGNED_ETHER_NEW_WALLET);
				} catch (Exception e) {
					LOGGER.log(Level.INFO, e.getMessage(), e);
				}
				return null;
			});
			// send to the client the new address...
			LOGGER.log(Level.INFO, "wallet created and with ether at address - " + newWalletAddress);
			return new Wallet(newWalletAddress);
		}
		throw new Exception("Can't create wallet file for address " + newWalletAddress + ", contact admin.");
	}

	/**
	 * 
	 * @param ownerWalletAddress
	 * @param ipfsHashId2
	 * @param condominiumId
	 * @param condominiumInfoJSON
	 * @return
	 * @throws Exception
	 */
	public Document uploadDocumentForRefuge(final String ownerWalletAddress, final String type, String ipfsHashId)
			throws Exception {
		final File walletFile = new File(WALLETS_PATH + File.separator + ownerWalletAddress + ".json");
		LOGGER.log(Level.INFO, "wallet of refuge " + walletFile.getAbsolutePath());
		if (walletFile.exists() && walletFile.canRead()) {
			String contractAddress = Main.ETHDOC_CONTRACT_ADDRESS_CLOUD;
			final Credentials credentials = WalletUtils.loadCredentials(NOVAHEMJO_COMMON_WALLET_PASSWORD, walletFile);
			final EtherDoc contract = EtherDoc.load(contractAddress, web3j, credentials, NOVAHEMJO_GAS_PRICE,
					NOVAHEMJO_GAS_LIMIT);
			LOGGER.log(Level.INFO, "contract address - " + contract.getContractAddress());
			LOGGER.log(Level.INFO, (contract.isValid()) ? "valid" : "invalid");
			LOGGER.log(Level.INFO, "ipfs hash id - " + ipfsHashId);
			// check that we have enough ether to execute transactions before calling the
			// contract...
			final EthGetBalance ethGetBalance = web3j
					.ethGetBalance(ownerWalletAddress, DefaultBlockParameterName.LATEST).send();
			final BigInteger wei = ethGetBalance.getBalance();
			LOGGER.log(Level.INFO, "balance of refuge wallet " + ownerWalletAddress + " in wei " + wei);
			// send create tx
			final TransactionReceipt uploadedDocumentTxReceipt = contract.newDocument(ipfsHashId.getBytes()).send();
			LOGGER.log(Level.INFO,
					"uploaded document for refuge is " + ((uploadedDocumentTxReceipt.isStatusOK()) ? "ok" : "not ok"));
			if (!uploadedDocumentTxReceipt.isStatusOK()) {
				LOGGER.log(Level.SEVERE, "failed creating document for owner wallet " + ownerWalletAddress + " reason "
						+ uploadedDocumentTxReceipt.getStatus());
				throw new Exception(uploadedDocumentTxReceipt.getStatus());
			}
			LOGGER.log(Level.INFO, "uploaded document with id " + ipfsHashId + " for owner " + ownerWalletAddress
					+ " and tx " + uploadedDocumentTxReceipt);
			// add new claim from document...in a background thread to finish
			CompletableFuture.supplyAsync(() -> {
				try {
					final Novahemjo novahemjo = Novahemjo.load(Main.ERC725_CONTRACT_ADDRESS_CLOUD, web3j, credentials,
							NOVAHEMJO_GAS_PRICE, NOVAHEMJO_GAS_LIMIT);
					LOGGER.log(Level.INFO, "contract address - " + novahemjo.getContractAddress());
					LOGGER.log(Level.INFO, (novahemjo.isValid()) ? "valid" : "invalid");
					// add key signature...
					final String signature = Hash.sha3(ownerWalletAddress);
					final TransactionReceipt addedKeyTxReceipt = novahemjo.addKey(signature, BigInteger.ZERO).send();
					LOGGER.log(Level.INFO, "refuge key is " + ((addedKeyTxReceipt.isStatusOK()) ? "ok" : "not ok"));
					if (!addedKeyTxReceipt.isStatusOK()) {
						LOGGER.log(Level.SEVERE, "failed creating claim for owner wallet " + ownerWalletAddress
								+ " reason " + addedKeyTxReceipt.getStatus());
						throw new Exception(addedKeyTxReceipt.getStatus());
					}
					// add claim...
					final byte[] _claim = ipfsHashId.getBytes();// data to store
					final byte[] _signature = new String(
							ownerWalletAddress + BigInteger.ZERO.toString() + new String(_claim)).getBytes(); // this.address
																												// +
																												// topic
																												// +
																												// data
					final TransactionReceipt claimAddedTxReceipt = novahemjo
							.addClaim(BigInteger.ZERO/* type */, "refuge"/* issuer */,
									BigInteger.ZERO/* signature type */, _signature, _claim, "ipfs://" + ipfsHashId)
							.send();
					LOGGER.log(Level.INFO,
							"claim added for refuge is " + ((claimAddedTxReceipt.isStatusOK()) ? "ok" : "not ok"));
					if (!claimAddedTxReceipt.isStatusOK()) {
						LOGGER.log(Level.SEVERE, "failed creating claim for owner wallet " + ownerWalletAddress
								+ " reason " + claimAddedTxReceipt.getStatus());
						throw new Exception(claimAddedTxReceipt.getStatus());
					}
				} catch (Exception e) {
					LOGGER.log(Level.INFO, e.getMessage(), e);
				}
				return null;
			});
			final BigInteger documentId = contract.getLatest().send();
			return new Document(documentId, ipfsHashId);
		}
		throw new Exception("Can't get wallet file for " + ownerWalletAddress + ", contact admin.");
	}

	/**
	 * 
	 * @param ownerWalletAddress
	 * @param condominiumId
	 * @return
	 */
	public Document getDocumentOfRefuge(final String ownerWalletAddress, final BigInteger documentId) throws Exception {
		final File walletFile = new File(WALLETS_PATH + File.separator + ownerWalletAddress + ".json");
		LOGGER.log(Level.INFO, "wallet of refuge " + walletFile.getAbsolutePath());
		if (walletFile.exists() && walletFile.canRead()) {
			String contractAddress = Main.ETHDOC_CONTRACT_ADDRESS_CLOUD;
			Credentials credentials = WalletUtils.loadCredentials(NOVAHEMJO_COMMON_WALLET_PASSWORD, walletFile);
			final EtherDoc contract = EtherDoc.load(contractAddress, web3j, credentials, NOVAHEMJO_GAS_PRICE,
					NOVAHEMJO_GAS_LIMIT);
			LOGGER.log(Level.INFO, "contract address - " + contract.getContractAddress());
			LOGGER.log(Level.INFO, (contract.isValid()) ? "valid" : "invalid");
			LOGGER.log(Level.INFO, "document id - " + documentId);
			final Tuple4<BigInteger/* blocknumber */, byte[]/* hash */, String/* from */, String/* to */> tuple = contract
					.getDocument(documentId).send();
			final IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
			final Multihash filePointer = Multihash.fromBase58(new String(tuple.getValue2()));
			final byte[] fileContents = ipfs.cat(filePointer);
			// return the document...
			Document document = new Document();
			document.setId(documentId.toString());
			document.setHashId(new String(tuple.getValue2()));
			document.setContents(new String(fileContents));
			LOGGER.log(Level.INFO, document.toString());
			return document;
		}
		throw new Exception("Can't get wallet file for " + ownerWalletAddress + ", contact admin.");
	}

	/**
	 * 
	 * @param ownerWalletAddress
	 * @param documentId
	 * @return
	 * @throws Exception
	 */
	public Tuple4<BigInteger, byte[], String, String> getDocumentHistory(final String ownerWalletAddress,
			final BigInteger documentId) throws Exception {
		final File walletFile = new File(WALLETS_PATH + File.separator + ownerWalletAddress + ".json");
		LOGGER.log(Level.INFO, "wallet of refuge " + walletFile.getAbsolutePath());
		if (walletFile.exists() && walletFile.canRead()) {
			String contractAddress = Main.ETHDOC_CONTRACT_ADDRESS_CLOUD;
			Credentials credentials = WalletUtils.loadCredentials(NOVAHEMJO_COMMON_WALLET_PASSWORD, walletFile);
			final EtherDoc contract = EtherDoc.load(contractAddress, web3j, credentials, NOVAHEMJO_GAS_PRICE,
					NOVAHEMJO_GAS_LIMIT);
			LOGGER.log(Level.INFO, "contract address - " + contract.getContractAddress());
			LOGGER.log(Level.INFO, (contract.isValid()) ? "valid" : "invalid");
			LOGGER.log(Level.INFO, "document id - " + documentId);
			final Tuple4<BigInteger/* blocknumber */, byte[]/* hash */, String/* from */, String/* to */> tuple = contract
					.getDocument(documentId).send();
			return tuple;
		}
		throw new Exception("Can't get wallet file for " + ownerWalletAddress + ", contact admin.");
	}

	/**
	 * 
	 * @param ownerWalletAddress
	 * @param refugeWalletAddress
	 * @return
	 * @throws Exception
	 */
	public Boolean addIssuerCredentials(final String ownerWalletAddress, final String refugeWalletAddress)
			throws Exception {
		final File walletFile = new File(WALLETS_PATH + File.separator + ownerWalletAddress + ".json");
		LOGGER.log(Level.INFO, "wallet of issuer " + walletFile.getAbsolutePath());
		if (walletFile.exists() && walletFile.canRead()) {
			String contractAddress = Main.ERC725_CONTRACT_ADDRESS_CLOUD;
			Credentials credentials = WalletUtils.loadCredentials(NOVAHEMJO_COMMON_WALLET_PASSWORD, walletFile);
			final Novahemjo contract = Novahemjo.load(contractAddress, web3j, credentials, NOVAHEMJO_GAS_PRICE,
					NOVAHEMJO_GAS_LIMIT);
			LOGGER.log(Level.INFO, "contract address - " + contract.getContractAddress());
			LOGGER.log(Level.INFO, (contract.isValid()) ? "valid" : "invalid");
			// from wallet add key...optionally get from parameter as an RSA key...
			final String issuerSignature = Hash.sha3(ownerWalletAddress);
			final TransactionReceipt addIssuerKeyTx = contract.addKey(issuerSignature, BigInteger.ONE).send();
			LOGGER.log(Level.INFO, "issuer key is " + ((addIssuerKeyTx.isStatusOK()) ? "ok" : "not ok"));
			if (!addIssuerKeyTx.isStatusOK()) {
				LOGGER.log(Level.SEVERE, "failed creating issuer key for owner wallet " + ownerWalletAddress
						+ " reason " + addIssuerKeyTx.getStatus());
				throw new Exception(addIssuerKeyTx.getStatus());
			}
			// now add the one from the refuge to parse claims...
			final String refugeSignature = Hash.sha3(refugeWalletAddress);
			final TransactionReceipt addRefugeKeyTx = contract.addKey(refugeSignature, BigInteger.ONE).send();
			LOGGER.log(Level.INFO, "refuge key for issuer is " + ((addRefugeKeyTx.isStatusOK()) ? "ok" : "not ok"));
			if (!addRefugeKeyTx.isStatusOK()) {
				LOGGER.log(Level.SEVERE, "failed creating refuge key for issuer for owner wallet " + ownerWalletAddress
						+ " reason " + addRefugeKeyTx.getStatus());
				throw new Exception(addRefugeKeyTx.getStatus());
			}
			return Boolean.TRUE;
		}
		throw new Exception("Can't get wallet file for " + ownerWalletAddress + ", contact admin.");
	}

	/**
	 * 
	 * @param ownerWalletAddress
	 * @param documentId
	 * @param claimerId
	 * @param claimType
	 * @throws Exception
	 */
	public Boolean claimDocumentFromRefuge(final String ownerWalletAddress, final BigInteger documentId,
			final String issuerWalletAddress) throws Exception {
		final File walletFile = new File(WALLETS_PATH + File.separator + ownerWalletAddress + ".json");
		LOGGER.log(Level.INFO, "wallet of refuge " + walletFile.getAbsolutePath());
		if (walletFile.exists() && walletFile.canRead()) {
			String contractAddress = Main.ETHDOC_CONTRACT_ADDRESS_CLOUD;
			Credentials credentials = WalletUtils.loadCredentials(NOVAHEMJO_COMMON_WALLET_PASSWORD, walletFile);
			final EtherDoc contract = EtherDoc.load(contractAddress, web3j, credentials, NOVAHEMJO_GAS_PRICE,
					NOVAHEMJO_GAS_LIMIT);
			LOGGER.log(Level.INFO, "contract address - " + contract.getContractAddress());
			LOGGER.log(Level.INFO, (contract.isValid()) ? "valid" : "invalid");
			LOGGER.log(Level.INFO, "document id - " + documentId);
			final Tuple4<BigInteger/* blocknumber */, byte[]/* hash */, String/* from */, String/* to */> tuple = contract
					.getDocument(documentId).send();
			final Novahemjo novahemjo = Novahemjo.load(Main.ERC725_CONTRACT_ADDRESS_CLOUD, web3j, credentials,
					NOVAHEMJO_GAS_PRICE, NOVAHEMJO_GAS_LIMIT);
			LOGGER.log(Level.INFO, "contract address - " + novahemjo.getContractAddress());
			LOGGER.log(Level.INFO, (novahemjo.isValid()) ? "valid" : "invalid");
			final byte[] _claimId = tuple.getValue2();// ipfs hash
			final Tuple6<BigInteger/* claim type */, String/* issuer */, BigInteger/* signature type */, byte[]/*
																												 * signature
																												 */, byte[]/*
																															 * claim
																															 */, String/*
																																		 * uri
																																		 */> refugeClaim = novahemjo
					.getClaim(_claimId).send();
			// check against claim issuers...for the moment approve it...
			final TransactionReceipt claimApprovedTxReceipt = novahemjo.approve(_claimId, Boolean.TRUE).send();
			LOGGER.log(Level.INFO,
					"claim added for refuge is " + ((claimApprovedTxReceipt.isStatusOK()) ? "ok" : "not ok"));
			if (!claimApprovedTxReceipt.isStatusOK()) {
				LOGGER.log(Level.SEVERE, "failed creating claim for owner wallet " + ownerWalletAddress + " reason "
						+ claimApprovedTxReceipt.getStatus());
				throw new Exception(claimApprovedTxReceipt.getStatus());
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	//
	private File getResourceAsFile(final String resourcePath) throws Exception {
		InputStream in = null;
		try {
			in = BlockchainFacade.class.getResourceAsStream(resourcePath);
			if (in == null) {
				return null;
			}
			File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
			tempFile.deleteOnExit();
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				// copy stream
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
			return tempFile;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// do nothing...
				}
			}
		}
	}

}
