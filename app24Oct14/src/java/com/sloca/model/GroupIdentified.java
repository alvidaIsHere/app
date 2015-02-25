/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import java.util.ArrayList;

/**
 *
 * @author jeremy.kuah.2013
 */
public class GroupIdentified {
 
    private ArrayList<Group> groupList;

    public int getTotalUser(){
        if(groupList == null){
            return 0;
        }else{
            int count = 0;
            
            for(Group g : groupList){
                count += g.getSize();
            }
            
            return count;
        }
    }
    
    public int getTotalGroup(){
        if(groupList == null){
            return 0;
        }else{
            return groupList.size();
        }
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }
    
    
}
