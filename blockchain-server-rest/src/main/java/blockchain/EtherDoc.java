package blockchain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>
 * Auto generated code.
 * <p>
 * <strong>Do not modify!</strong>
 * <p>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j
 * command line tools</a>, or the
 * org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen
 * module</a> to update.
 *
 * <p>
 * Generated with web3j version 3.4.0.
 */
public class EtherDoc extends Contract {
	private static final String BINARY = "6060604052341561000f57600080fd5b60008054600160a060020a033316600160a060020a03199091161790556104d88061003b6000396000f3006060604052600436106100985763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306ae19a2811461009d5780632847a7af146100cf5780633f9b250a146100f9578063a7a38f0b14610145578063aef18bf71461015b578063c36af46014610171578063cbfe6d4114610196578063deaf5a5e146101ac578063f2a75fe4146101ce575b600080fd5b34156100a857600080fd5b6100b36004356101e3565b604051600160a060020a03909116815260200160405180910390f35b34156100da57600080fd5b6100e56004356101fe565b604051901515815260200160405180910390f35b341561010457600080fd5b61010f600435610244565b6040519384526020840192909252600160a060020a03908116604080850191909152911660608301526080909101905180910390f35b341561015057600080fd5b61010f60043561027a565b341561016657600080fd5b6100e56004356102af565b341561017c57600080fd5b6101846102c4565b60405190815260200160405180910390f35b34156101a157600080fd5b6100e56004356102ca565b34156101b757600080fd5b6100e5600435600160a060020a03602435166102f1565b34156101d957600080fd5b6101e1610339565b005b600460205260009081526040902054600160a060020a031681565b6000610209826102ca565b156102165750600061023f565b610221823333610369565b506000818152600360205260409020805460ff191660019081179091555b919050565b600090815260026020819052604090912080546001820154928201546003909201549093600160a060020a039283169290911690565b60026020819052600091825260409091208054600182015492820154600390920154909291600160a060020a03908116911684565b60036020526000908152604090205460ff1681565b60015490565b60008181526003602052604081205460ff16156102e95750600161023f565b506000919050565b60006102fc836102ca565b156103335760008381526004602052604090205433600160a060020a03908116911614156103335761032f833384610369565b5060015b92915050565b600054600160a060020a0330811631911681156108fc0282604051600060405180830381858888f1505050505050565b60018054810181556000848152600460209081526040808320805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a038716179055600390915290819020805460ff19169092179091556080905190810160409081524382526020808301869052600160a060020a03808616838501528416606084015260015460009081526002909152208151815560208201516001820155604082015160028201805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03929092169190911790556060820151600391909101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392831617905582811691508316847f7eb8c51c420936187e4556bc844b0f224888f66ea225667930ccd4f3b088389f4360405190815260200160405180910390a45050505600a165627a7a7230582039c48c537966213793bbbcc621b5cdef27595d9ed6fe50c41b992b813404f64a0029";

	public static final String FUNC_DOCUMENTHASHMAP = "documentHashMap";

	public static final String FUNC_NEWDOCUMENT = "newDocument";

	public static final String FUNC_GETDOCUMENT = "getDocument";

	public static final String FUNC_HISTORY = "history";

	public static final String FUNC_USEDHASHES = "usedHashes";

	public static final String FUNC_GETLATEST = "getLatest";

	public static final String FUNC_DOCUMENTEXISTS = "documentExists";

	public static final String FUNC_TRANSFERDOCUMENT = "transferDocument";

	public static final String FUNC_EMPTY = "empty";

	public static final Event DOCUMENTEVENT_EVENT = new Event("DocumentEvent",
			Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
			}, new TypeReference<Address>() {
			}, new TypeReference<Address>() {
			}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
			}));;

	@SuppressWarnings("deprecation")
	protected EtherDoc(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
			BigInteger gasLimit) {
		super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
	}

	@SuppressWarnings("deprecation")
	protected EtherDoc(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice,
			BigInteger gasLimit) {
		super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<String> documentHashMap(byte[] param0) {
		final Function function = new Function(FUNC_DOCUMENTHASHMAP,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
				}));
		return executeRemoteCallSingleValueReturn(function, String.class);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<TransactionReceipt> newDocument(byte[] hash) {
		final Function function = new Function(FUNC_NEWDOCUMENT,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)),
				Collections.<TypeReference<?>>emptyList());
		return executeRemoteCallTransaction(function);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<Tuple4<BigInteger, byte[], String, String>> getDocument(BigInteger docId) {
		final Function function = new Function(FUNC_GETDOCUMENT,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(docId)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}, new TypeReference<Bytes32>() {
				}, new TypeReference<Address>() {
				}, new TypeReference<Address>() {
				}));
		return new RemoteCall<Tuple4<BigInteger, byte[], String, String>>(
				new Callable<Tuple4<BigInteger, byte[], String, String>>() {
					@Override
					public Tuple4<BigInteger, byte[], String, String> call() throws Exception {
						List<Type> results = executeCallMultipleValueReturn(function);
						return new Tuple4<BigInteger, byte[], String, String>((BigInteger) results.get(0).getValue(),
								(byte[]) results.get(1).getValue(), (String) results.get(2).getValue(),
								(String) results.get(3).getValue());
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<Tuple4<BigInteger, byte[], String, String>> history(BigInteger param0) {
		final Function function = new Function(FUNC_HISTORY,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}, new TypeReference<Bytes32>() {
				}, new TypeReference<Address>() {
				}, new TypeReference<Address>() {
				}));
		return new RemoteCall<Tuple4<BigInteger, byte[], String, String>>(
				new Callable<Tuple4<BigInteger, byte[], String, String>>() {
					@Override
					public Tuple4<BigInteger, byte[], String, String> call() throws Exception {
						List<Type> results = executeCallMultipleValueReturn(function);
						return new Tuple4<BigInteger, byte[], String, String>((BigInteger) results.get(0).getValue(),
								(byte[]) results.get(1).getValue(), (String) results.get(2).getValue(),
								(String) results.get(3).getValue());
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<Boolean> usedHashes(byte[] param0) {
		final Function function = new Function(FUNC_USEDHASHES,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
				}));
		return executeRemoteCallSingleValueReturn(function, Boolean.class);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<BigInteger> getLatest() {
		final Function function = new Function(FUNC_GETLATEST, Arrays.<Type>asList(),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));
		return executeRemoteCallSingleValueReturn(function, BigInteger.class);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<Boolean> documentExists(byte[] hash) {
		final Function function = new Function(FUNC_DOCUMENTEXISTS,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
				}));
		return executeRemoteCallSingleValueReturn(function, Boolean.class);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<TransactionReceipt> transferDocument(byte[] hash, String recipient) {
		final Function function = new Function(FUNC_TRANSFERDOCUMENT,
				Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash),
						new org.web3j.abi.datatypes.Address(recipient)),
				Collections.<TypeReference<?>>emptyList());
		return executeRemoteCallTransaction(function);
	}

	@SuppressWarnings("rawtypes")
	public RemoteCall<TransactionReceipt> empty() {
		final Function function = new Function(FUNC_EMPTY, Arrays.<Type>asList(),
				Collections.<TypeReference<?>>emptyList());
		return executeRemoteCallTransaction(function);
	}

	public static RemoteCall<EtherDoc> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice,
			BigInteger gasLimit) {
		return deployRemoteCall(EtherDoc.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
	}

	public static RemoteCall<EtherDoc> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice,
			BigInteger gasLimit) {
		return deployRemoteCall(EtherDoc.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
	}

	public List<DocumentEventEventResponse> getDocumentEventEvents(TransactionReceipt transactionReceipt) {
		List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DOCUMENTEVENT_EVENT,
				transactionReceipt);
		ArrayList<DocumentEventEventResponse> responses = new ArrayList<DocumentEventEventResponse>(valueList.size());
		for (Contract.EventValuesWithLog eventValues : valueList) {
			DocumentEventEventResponse typedResponse = new DocumentEventEventResponse();
			typedResponse.log = eventValues.getLog();
			typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
			typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
			typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
			typedResponse.blockNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
			responses.add(typedResponse);
		}
		return responses;
	}

	public Observable<DocumentEventEventResponse> documentEventEventObservable(EthFilter filter) {
		return web3j.ethLogObservable(filter).map(new Func1<Log, DocumentEventEventResponse>() {
			@Override
			public DocumentEventEventResponse call(Log log) {
				Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DOCUMENTEVENT_EVENT, log);
				DocumentEventEventResponse typedResponse = new DocumentEventEventResponse();
				typedResponse.log = log;
				typedResponse.hash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
				typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
				typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
				typedResponse.blockNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
				return typedResponse;
			}
		});
	}

	public Observable<DocumentEventEventResponse> documentEventEventObservable(DefaultBlockParameter startBlock,
			DefaultBlockParameter endBlock) {
		EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
		filter.addSingleTopic(EventEncoder.encode(DOCUMENTEVENT_EVENT));
		return documentEventEventObservable(filter);
	}

	public static EtherDoc load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
			BigInteger gasLimit) {
		return new EtherDoc(contractAddress, web3j, credentials, gasPrice, gasLimit);
	}

	public static EtherDoc load(String contractAddress, Web3j web3j, TransactionManager transactionManager,
			BigInteger gasPrice, BigInteger gasLimit) {
		return new EtherDoc(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
	}

	public static class DocumentEventEventResponse {
		public Log log;

		public byte[] hash;

		public String from;

		public String to;

		public BigInteger blockNumber;
	}
}
