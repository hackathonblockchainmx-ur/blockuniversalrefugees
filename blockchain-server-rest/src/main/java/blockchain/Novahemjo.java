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
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.4.0.
 */
public class Novahemjo extends Contract {
    private static final String BINARY = "6080604052600060065534801561001557600080fd5b5061002a33600164010000000061002f810204565b610108565b600160a060020a0382166000908152602081905260409020541561005257600080fd5b6040518190600160a060020a038416907f7d958a859734aa5212d2568f8700fe77619bc93d5b08abf1445585bac8bff60690600090a3600160a060020a039091166000818152602081815260408083208590558483526002808352818420805483516c010000000000000000000000008802815260148101989098528351978890036034019097208552600484529184208690558252600185018155825290209091018054600160a060020a0319169091179055565b6113f6806101176000396000f3006080604052600436106100ae5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630607f93781146100b357806341cbfc7b146101b05780634b24fd0d146102185780634ec79937146102395780634eee424a1461026a57806369e78499146102825780636fa28249146102a3578063a2d39bdb146102bb578063b53ea1b6146102df578063b61d27f614610306578063c9100bcb1461036f575b600080fd5b3480156100bf57600080fd5b50604080516020601f60643560048181013592830184900484028501840190955281845261019e94803594600160a060020a03602480359190911695604435953695608494930191819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a99988101979196509182019450925082915084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506104f29650505050505050565b60408051918252519081900360200190f35b3480156101bc57600080fd5b506101c8600435610673565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156102045781810151838201526020016101ec565b505050509050019250505060405180910390f35b34801561022457600080fd5b5061019e600160a060020a03600435166106e0565b34801561024557600080fd5b5061025660043560243515156106fb565b604080519115158252519081900360200190f35b34801561027657600080fd5b506102566004356107f9565b34801561028e57600080fd5b50610256600160a060020a0360043516610b0e565b3480156102af57600080fd5b506101c8600435610b33565b3480156102c757600080fd5b50610256600160a060020a0360043516602435610b95565b3480156102eb57600080fd5b50610256600160a060020a0360043581169060243516610bbb565b34801561031257600080fd5b50604080516020600460443581810135601f810184900484028501840190955284845261019e948235600160a060020a0316946024803595369594606494920191908190840183828082843750949750610bf29650505050505050565b34801561037b57600080fd5b50610387600435610dd6565b6040518087815260200186600160a060020a0316600160a060020a03168152602001858152602001806020018060200180602001848103845287818151815260200191508051906020019080838360005b838110156103f05781810151838201526020016103d8565b50505050905090810190601f16801561041d5780820380516001836020036101000a031916815260200191505b50848103835286518152865160209182019188019080838360005b83811015610450578181015183820152602001610438565b50505050905090810190601f16801561047d5780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b838110156104b0578181015183820152602001610498565b50505050905090810190601f1680156104dd5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b3360009081526020819052604081205460031461050e57600080fd5b50604080516c01000000000000000000000000600160a060020a038089169182028352601483018a9052835192839003603401832060c0840185528a845260208481019384528486018a8152606086018a8152608087018a905260a087018990526000848152600180855298902087518155955197860180549890951673ffffffffffffffffffffffffffffffffffffffff199098169790971790935591516002840155935180516105c6926003850192019061127f565b50608082015180516105e291600484019160209091019061127f565b5060a082015180516105fe91600584019160209091019061127f565b5050506000878152600360208181526040808420805482516c01000000000000000000000000600160a060020a03909d169c909c028c5260148c019c909c5281519a8b9003603401909a2084526004825283208a90559081526001890188559681529590952090950184905550919392505050565b6000818152600260209081526040918290208054835181840281018401909452808452606093928301828280156106d357602002820191906000526020600020905b8154600160a060020a031681526001909101906020018083116106b5575b505050505090505b919050565b600160a060020a031660009081526020819052604090205490565b3360009081526020819052604081205460011461071757600080fd5b60065460008481526005602052604090206003015460019091011461073b57600080fd5b60068054600101905581156107f357600083815260056020526040908190208054600180830154935160029384018054600160a060020a0390941695949093919283928592918116156101000260001901160480156107db5780601f106107b0576101008083540402835291602001916107db565b820191906000526020600020905b8154815290600101906020018083116107be57829003601f168201915b505091505060006040518083038185875af193505050505b92915050565b60006108036112fd565b336000908152602081905260408120548190819060031461082357600080fd5b600086815260016020818152604092839020835160c0810185528154815281840154600160a060020a031681840152600280830154828701526003830180548751601f978216156101000260001901909116929092049586018590048502820185019096528481529094919360608601939192918301828280156108e85780601f106108bd576101008083540402835291602001916108e8565b820191906000526020600020905b8154815290600101906020018083116108cb57829003601f168201915b505050918352505060048201805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815293820193929183018282801561097c5780601f106109515761010080835404028352916020019161097c565b820191906000526020600020905b81548152906001019060200180831161095f57829003601f168201915b505050918352505060058201805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152938201939291830182828015610a105780601f106109e557610100808354040283529160200191610a10565b820191906000526020600020905b8154815290600101906020018083116109f357829003601f168201915b5050509190925250505060008781526004602090815260408083208054908490558451845260039092529091208054929650909450925082906000198101908110610a5757fe5b90600052602060002001549050808284815481101515610a7357fe5b60009182526020808320909101929092558281526004825260408082208690558882526001928390528120818155918201805473ffffffffffffffffffffffffffffffffffffffff1916905560028201819055610ad3600383018261133d565b610ae160048301600061133d565b610aef60058301600061133d565b50508154610b01836000198301611384565b5060019695505050505050565b33600090815260208190526040812054600114610b2a57600080fd5b6106db82611011565b6000818152600360209081526040918290208054835181840281018401909452808452606093928301828280156106d357602002820191906000526020600020905b81548152600190910190602001808311610b755750505050509050919050565b33600090815260208190526040812054600114610bb157600080fd5b6107f38383611199565b33600090815260208190526040812054600114610bd757600080fd5b610be982610be4856106e0565b611199565b6107f383611011565b33600090815260208190526040812054600214610c0e57600080fd5b8383836006546001016040518085600160a060020a0316600160a060020a03166c0100000000000000000000000002815260140184815260200183805190602001908083835b60208310610c735780518252601f199092019160209182019101610c54565b51815160209384036101000a600019018019909216911617905292019384525060408051938490038201842082855289518584015289519098508a9750600160a060020a038c1696508895507fce0206f766cbb69a1ad1b8485f947bf53c2c0a2f3cf1078b31bb424833d3b0fb948a94509283928301919085019080838360005b83811015610d0c578181015183820152602001610cf4565b50505050905090810190601f168015610d395780820380516001836020036101000a031916815260200191505b509250505060405180910390a460408051608081018252600160a060020a03868116825260208083018781528385018781526006546060860152600087815260058452959095208451815473ffffffffffffffffffffffffffffffffffffffff191694169390931783555160018301559251805192939192610dc1926002850192019061127f565b50606082015181600301559050509392505050565b60008060006060806060610de86112fd565b600088815260016020818152604092839020835160c0810185528154815281840154600160a060020a031681840152600280830154828701526003830180548751601f97821615610100026000190190911692909204958601859004850282018501909652848152909491936060860193919291830182828015610ead5780601f10610e8257610100808354040283529160200191610ead565b820191906000526020600020905b815481529060010190602001808311610e9057829003601f168201915b505050918352505060048201805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152938201939291830182828015610f415780601f10610f1657610100808354040283529160200191610f41565b820191906000526020600020905b815481529060010190602001808311610f2457829003601f168201915b505050918352505060058201805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152938201939291830182828015610fd55780601f10610faa57610100808354040283529160200191610fd5565b820191906000526020600020905b815481529060010190602001808311610fb857829003601f168201915b5050509190925250508151602083015160408401516060850151608086015160a090960151939e929d50909b5099509297509550909350505050565b600160a060020a03811660008181526020819052604080822054905190928291829182918691907fe96ba5805e91ce4b5225d90ad1aac15c207472188f51f24025974341360f0f8a908490a3600085815260026020908152604080832081516c01000000000000000000000000600160a060020a038c16028152601481018a90528251908190036034019020808552600490935290832080549390558054909650909450909250849060001981019081106110c857fe5b6000918252602090912001548454600160a060020a03909116915081908590849081106110f157fe5b600091825260208083209091018054600160a060020a0394851673ffffffffffffffffffffffffffffffffffffffff1990911617905591831680825281835260408083205481516c010000000000000000000000009093028352601483015280519182900360340190912082526004909252208290558354611177856000198301611384565b505050600160a060020a03909316600090815260208190526040812055505050565b600160a060020a038216600090815260208190526040902054156111bc57600080fd5b6040518190600160a060020a038416907f7d958a859734aa5212d2568f8700fe77619bc93d5b08abf1445585bac8bff60690600090a3600160a060020a039091166000818152602081815260408083208590558483526002808352818420805483516c01000000000000000000000000880281526014810198909852835197889003603401909720855260048452918420869055825260018501815582529020909101805473ffffffffffffffffffffffffffffffffffffffff19169091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106112c057805160ff19168380011785556112ed565b828001600101855582156112ed579182015b828111156112ed5782518255916020019190600101906112d2565b506112f99291506113ad565b5090565b60c060405190810160405280600081526020016000600160a060020a03168152602001600081526020016060815260200160608152602001606081525090565b50805460018160011615610100020316600290046000825580601f106113635750611381565b601f01602090049060005260206000209081019061138191906113ad565b50565b8154818355818111156113a8576000838152602090206113a89181019083016113ad565b505050565b6113c791905b808211156112f957600081556001016113b3565b905600a165627a7a7230582040c5d05134da282b874ae7d27ad4b7897c7f4b2ab48c526cd26a338bb3c241980029\",";

    public static final String FUNC_ADDCLAIM = "addClaim";

    public static final String FUNC_ADDKEY = "addKey";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_EXECUTE = "execute";

    public static final String FUNC_REMOVECLAIM = "removeClaim";

    public static final String FUNC_REMOVEKEY = "removeKey";

    public static final String FUNC_REPLACEKEY = "replaceKey";

    public static final String FUNC_GETCLAIM = "getClaim";

    public static final String FUNC_GETCLAIMSIDBYTYPE = "getClaimsIdByType";

    public static final String FUNC_GETKEYSBYTYPE = "getKeysByType";

    public static final String FUNC_GETKEYTYPE = "getKeyType";

    public static final Event CLAIMREQUESTED_EVENT = new Event("ClaimRequested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event CLAIMADDED_EVENT = new Event("ClaimAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event CLAIMREMOVED_EVENT = new Event("ClaimRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event CLAIMCHANGED_EVENT = new Event("ClaimChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event KEYADDED_EVENT = new Event("KeyAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event KEYREMOVED_EVENT = new Event("KeyRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event KEYREPLACED_EVENT = new Event("KeyReplaced", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event EXECUTIONREQUESTED_EVENT = new Event("ExecutionRequested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event EXECUTED_EVENT = new Event("Executed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event APPROVED_EVENT = new Event("Approved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
    ;

    @SuppressWarnings("deprecation")
	protected Novahemjo(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @SuppressWarnings("deprecation")
    protected Novahemjo(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    
    @SuppressWarnings("rawtypes")
	public RemoteCall<TransactionReceipt> addClaim(BigInteger _claimType, String _issuer, BigInteger _signatureType, byte[] _signature, byte[] _claim, String _uri) {
        final Function function = new Function(
                FUNC_ADDCLAIM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_claimType), 
                new org.web3j.abi.datatypes.Address(_issuer), 
                new org.web3j.abi.datatypes.generated.Uint256(_signatureType), 
                new org.web3j.abi.datatypes.DynamicBytes(_signature), 
                new org.web3j.abi.datatypes.DynamicBytes(_claim), 
                new org.web3j.abi.datatypes.Utf8String(_uri)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<TransactionReceipt> addKey(String _key, BigInteger _type) {
        final Function function = new Function(
                FUNC_ADDKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_key), 
                new org.web3j.abi.datatypes.generated.Uint256(_type)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<TransactionReceipt> approve(byte[] _id, Boolean _approve) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_id), 
                new org.web3j.abi.datatypes.Bool(_approve)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<ClaimRequestedEventResponse> getClaimRequestedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIMREQUESTED_EVENT, transactionReceipt);
        ArrayList<ClaimRequestedEventResponse> responses = new ArrayList<ClaimRequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ClaimRequestedEventResponse typedResponse = new ClaimRequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ClaimRequestedEventResponse> claimRequestedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ClaimRequestedEventResponse>() {
            @Override
            public ClaimRequestedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIMREQUESTED_EVENT, log);
                ClaimRequestedEventResponse typedResponse = new ClaimRequestedEventResponse();
                typedResponse.log = log;
                typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ClaimRequestedEventResponse> claimRequestedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIMREQUESTED_EVENT));
        return claimRequestedEventObservable(filter);
    }

    public List<ClaimAddedEventResponse> getClaimAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIMADDED_EVENT, transactionReceipt);
        ArrayList<ClaimAddedEventResponse> responses = new ArrayList<ClaimAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ClaimAddedEventResponse typedResponse = new ClaimAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ClaimAddedEventResponse> claimAddedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ClaimAddedEventResponse>() {
            @Override
            public ClaimAddedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIMADDED_EVENT, log);
                ClaimAddedEventResponse typedResponse = new ClaimAddedEventResponse();
                typedResponse.log = log;
                typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ClaimAddedEventResponse> claimAddedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIMADDED_EVENT));
        return claimAddedEventObservable(filter);
    }

    public List<ClaimRemovedEventResponse> getClaimRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIMREMOVED_EVENT, transactionReceipt);
        ArrayList<ClaimRemovedEventResponse> responses = new ArrayList<ClaimRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ClaimRemovedEventResponse typedResponse = new ClaimRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ClaimRemovedEventResponse> claimRemovedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ClaimRemovedEventResponse>() {
            @Override
            public ClaimRemovedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIMREMOVED_EVENT, log);
                ClaimRemovedEventResponse typedResponse = new ClaimRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ClaimRemovedEventResponse> claimRemovedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIMREMOVED_EVENT));
        return claimRemovedEventObservable(filter);
    }

    public List<ClaimChangedEventResponse> getClaimChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CLAIMCHANGED_EVENT, transactionReceipt);
        ArrayList<ClaimChangedEventResponse> responses = new ArrayList<ClaimChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ClaimChangedEventResponse typedResponse = new ClaimChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ClaimChangedEventResponse> claimChangedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ClaimChangedEventResponse>() {
            @Override
            public ClaimChangedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CLAIMCHANGED_EVENT, log);
                ClaimChangedEventResponse typedResponse = new ClaimChangedEventResponse();
                typedResponse.log = log;
                typedResponse.claimId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.claimType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.issuer = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.signatureType = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signature = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.claim = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.uri = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ClaimChangedEventResponse> claimChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CLAIMCHANGED_EVENT));
        return claimChangedEventObservable(filter);
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<TransactionReceipt> execute(String _to, BigInteger _value, byte[] _data) {
        final Function function = new Function(
                FUNC_EXECUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value), 
                new org.web3j.abi.datatypes.DynamicBytes(_data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<KeyAddedEventResponse> getKeyAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(KEYADDED_EVENT, transactionReceipt);
        ArrayList<KeyAddedEventResponse> responses = new ArrayList<KeyAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            KeyAddedEventResponse typedResponse = new KeyAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.key = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.keyType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<KeyAddedEventResponse> keyAddedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, KeyAddedEventResponse>() {
            @Override
            public KeyAddedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(KEYADDED_EVENT, log);
                KeyAddedEventResponse typedResponse = new KeyAddedEventResponse();
                typedResponse.log = log;
                typedResponse.key = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.keyType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<KeyAddedEventResponse> keyAddedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(KEYADDED_EVENT));
        return keyAddedEventObservable(filter);
    }

    public List<KeyRemovedEventResponse> getKeyRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(KEYREMOVED_EVENT, transactionReceipt);
        ArrayList<KeyRemovedEventResponse> responses = new ArrayList<KeyRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            KeyRemovedEventResponse typedResponse = new KeyRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.key = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.keyType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<KeyRemovedEventResponse> keyRemovedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, KeyRemovedEventResponse>() {
            @Override
            public KeyRemovedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(KEYREMOVED_EVENT, log);
                KeyRemovedEventResponse typedResponse = new KeyRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.key = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.keyType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<KeyRemovedEventResponse> keyRemovedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(KEYREMOVED_EVENT));
        return keyRemovedEventObservable(filter);
    }

    public List<KeyReplacedEventResponse> getKeyReplacedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(KEYREPLACED_EVENT, transactionReceipt);
        ArrayList<KeyReplacedEventResponse> responses = new ArrayList<KeyReplacedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            KeyReplacedEventResponse typedResponse = new KeyReplacedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.oldKey = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newKey = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.keyType = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<KeyReplacedEventResponse> keyReplacedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, KeyReplacedEventResponse>() {
            @Override
            public KeyReplacedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(KEYREPLACED_EVENT, log);
                KeyReplacedEventResponse typedResponse = new KeyReplacedEventResponse();
                typedResponse.log = log;
                typedResponse.oldKey = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newKey = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.keyType = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<KeyReplacedEventResponse> keyReplacedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(KEYREPLACED_EVENT));
        return keyReplacedEventObservable(filter);
    }

    public List<ExecutionRequestedEventResponse> getExecutionRequestedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(EXECUTIONREQUESTED_EVENT, transactionReceipt);
        ArrayList<ExecutionRequestedEventResponse> responses = new ArrayList<ExecutionRequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ExecutionRequestedEventResponse typedResponse = new ExecutionRequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.executionId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ExecutionRequestedEventResponse> executionRequestedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ExecutionRequestedEventResponse>() {
            @Override
            public ExecutionRequestedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(EXECUTIONREQUESTED_EVENT, log);
                ExecutionRequestedEventResponse typedResponse = new ExecutionRequestedEventResponse();
                typedResponse.log = log;
                typedResponse.executionId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ExecutionRequestedEventResponse> executionRequestedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EXECUTIONREQUESTED_EVENT));
        return executionRequestedEventObservable(filter);
    }

    public List<ExecutedEventResponse> getExecutedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(EXECUTED_EVENT, transactionReceipt);
        ArrayList<ExecutedEventResponse> responses = new ArrayList<ExecutedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ExecutedEventResponse typedResponse = new ExecutedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.executionId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ExecutedEventResponse> executedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ExecutedEventResponse>() {
            @Override
            public ExecutedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(EXECUTED_EVENT, log);
                ExecutedEventResponse typedResponse = new ExecutedEventResponse();
                typedResponse.log = log;
                typedResponse.executionId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ExecutedEventResponse> executedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EXECUTED_EVENT));
        return executedEventObservable(filter);
    }

    public List<ApprovedEventResponse> getApprovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVED_EVENT, transactionReceipt);
        ArrayList<ApprovedEventResponse> responses = new ArrayList<ApprovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovedEventResponse typedResponse = new ApprovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.executionId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovedEventResponse> approvedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovedEventResponse>() {
            @Override
            public ApprovedEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVED_EVENT, log);
                ApprovedEventResponse typedResponse = new ApprovedEventResponse();
                typedResponse.log = log;
                typedResponse.executionId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ApprovedEventResponse> approvedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVED_EVENT));
        return approvedEventObservable(filter);
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<TransactionReceipt> removeClaim(byte[] _claimId) {
        final Function function = new Function(
                FUNC_REMOVECLAIM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_claimId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<TransactionReceipt> removeKey(String _key) {
        final Function function = new Function(
                FUNC_REMOVEKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<TransactionReceipt> replaceKey(String _oldKey, String _newKey) {
        final Function function = new Function(
                FUNC_REPLACEKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_oldKey), 
                new org.web3j.abi.datatypes.Address(_newKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Novahemjo> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Novahemjo.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Novahemjo> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Novahemjo.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<Tuple6<BigInteger, String, BigInteger, byte[], byte[], String>> getClaim(byte[] _claimId) {
        final Function function = new Function(FUNC_GETCLAIM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_claimId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple6<BigInteger, String, BigInteger, byte[], byte[], String>>(
                new Callable<Tuple6<BigInteger, String, BigInteger, byte[], byte[], String>>() {
                    @Override
                    public Tuple6<BigInteger, String, BigInteger, byte[], byte[], String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<BigInteger, String, BigInteger, byte[], byte[], String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (byte[]) results.get(4).getValue(), 
                                (String) results.get(5).getValue());
                    }
                });
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<List> getClaimsIdByType(BigInteger _claimType) {
        final Function function = new Function(FUNC_GETCLAIMSIDBYTYPE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_claimType)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<List> getKeysByType(BigInteger _type) {
        final Function function = new Function(FUNC_GETKEYSBYTYPE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_type)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @SuppressWarnings("rawtypes")
    public RemoteCall<BigInteger> getKeyType(String _key) {
        final Function function = new Function(FUNC_GETKEYTYPE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_key)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static Novahemjo load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Novahemjo(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Novahemjo load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Novahemjo(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class ClaimRequestedEventResponse {
        public Log log;

        public byte[] claimId;

        public BigInteger claimType;

        public String issuer;

        public BigInteger signatureType;

        public byte[] signature;

        public byte[] claim;

        public String uri;
    }

    public static class ClaimAddedEventResponse {
        public Log log;

        public byte[] claimId;

        public BigInteger claimType;

        public String issuer;

        public BigInteger signatureType;

        public byte[] signature;

        public byte[] claim;

        public String uri;
    }

    public static class ClaimRemovedEventResponse {
        public Log log;

        public byte[] claimId;

        public BigInteger claimType;

        public String issuer;

        public BigInteger signatureType;

        public byte[] signature;

        public byte[] claim;

        public String uri;
    }

    public static class ClaimChangedEventResponse {
        public Log log;

        public byte[] claimId;

        public BigInteger claimType;

        public String issuer;

        public BigInteger signatureType;

        public byte[] signature;

        public byte[] claim;

        public String uri;
    }

    public static class KeyAddedEventResponse {
        public Log log;

        public String key;

        public BigInteger keyType;
    }

    public static class KeyRemovedEventResponse {
        public Log log;

        public String key;

        public BigInteger keyType;
    }

    public static class KeyReplacedEventResponse {
        public Log log;

        public String oldKey;

        public String newKey;

        public BigInteger keyType;
    }

    public static class ExecutionRequestedEventResponse {
        public Log log;

        public byte[] executionId;

        public String to;

        public BigInteger value;

        public byte[] data;
    }

    public static class ExecutedEventResponse {
        public Log log;

        public byte[] executionId;

        public String to;

        public BigInteger value;

        public byte[] data;
    }

    public static class ApprovedEventResponse {
        public Log log;

        public byte[] executionId;

        public Boolean approved;
    }
}
