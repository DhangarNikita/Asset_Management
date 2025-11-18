package com.asset.AssetManagement.exception;

public class DuplicateAssetException extends RuntimeException {
    public DuplicateAssetException(String msg){
        super(msg);
    }
}
