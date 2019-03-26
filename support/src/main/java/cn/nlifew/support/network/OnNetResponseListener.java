package cn.nlifew.support.network;

public interface OnNetResponseListener {
    void onNetResponse(NetRequest request) throws Exception;
    void onNetFailed(NetRequest request, Exception exp);
}
