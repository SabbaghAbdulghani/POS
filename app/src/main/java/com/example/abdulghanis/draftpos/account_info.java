package com.example.abdulghanis.draftpos;

public class account_info {
    public String account_id;
    public String account_code ;
    public String account_name ;
    public String account_parent ;
    public String parent_name;
    public String getlongName() {
        return account_code + "-" + account_name;
    }
    public byte account_type ;
    public byte net_type ;
    public double balance;
    public byte[] logo ;
}
