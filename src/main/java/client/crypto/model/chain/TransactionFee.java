package client.crypto.model.chain;

import client.crypto.model.types.EosType;
import client.crypto.model.types.TypeAsset;
import client.util.Utils;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by swapnibble on 2018-03-19.
 */

public class TransactionFee extends TransactionFeeHeader {
    //@Expose
    private List<Action> context_free_actions = new ArrayList<>();

    //@Expose
    private List<Action> actions = new ArrayList<>();

    // Extentions are prefixed with type and are a buffer that can be interpreted by code that is aware and ignored by unaware code.
    //@Expose
    private List<String> transaction_extensions = new ArrayList<>();


    public TransactionFee(){
        super();
    }


    public TransactionFee(TransactionFee other) {
        super(other);
        this.context_free_actions = deepCopyOnlyContainer( other.context_free_actions );
        this.actions = deepCopyOnlyContainer( other.actions );
        this.transaction_extensions = other.transaction_extensions;
    }

    public void addAction(Action msg ){
        if ( null == actions) {
            actions = new ArrayList<>(1);
        }

        actions.add( msg);
    }


    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public int getContextFreeActionCount(){ return ( actions == null ? 0 : actions.size());}


    <T> List<T> deepCopyOnlyContainer(List<T> srcList){
        if ( null == srcList ){
            return null;
        }

        List<T> newList = new ArrayList<>( srcList.size() );
        newList.addAll( srcList);

        return newList;
    }

    @Override
    public void pack(EosType.Writer writer) {
        super.pack(writer);

        writer.putCollection(context_free_actions);
        writer.putCollection(actions);
        //writer.putCollection(transaction_extensions);
        writer.putVariableUInt( transaction_extensions.size());
        if ( transaction_extensions.size() > 0 ){
            // TODO 구체적 코드가 나오면 확인후 구현할 것.
        }

        long f = (long) (10000 * Utils.matchFloat(getFee()));
        TypeAsset asset = new TypeAsset(f);
        writer.putLongLE(asset.getAmount());
        writer.putLongLE(asset.assetSymbol());
    }

    public List<Action> getContext_free_actions() {
        return context_free_actions;
    }

    public void setContext_free_actions(List<Action> context_free_actions) {
        this.context_free_actions = context_free_actions;
    }

    public List<String> getTransaction_extensions() {
        return transaction_extensions;
    }

    public void setTransaction_extensions(List<String> transaction_extensions) {
        this.transaction_extensions = transaction_extensions;
    }


}
