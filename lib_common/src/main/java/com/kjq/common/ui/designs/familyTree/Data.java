package com.kjq.common.ui.designs.familyTree;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.kjq.common.utils.application.BaseApplication;
import com.kjq.common.utils.AssetsUtil;
import com.kjq.common.utils.DateAndTimeUtil;
import com.kjq.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Data  {

    private List<FamilyBean> mList;
    private static boolean mInquirySpouse = false;//是否查询配偶

    private Data() {
        String json = AssetsUtil.getAssetsTxtByName(Utils.getContext(), "family_tree.txt");
        mList = JSONObject.parseArray(json, FamilyBean.class);
    }

    public static Data getInstance() {
        Data sData = (Data) BaseApplication.getInstance().getArrayValue(1111111);

        if (sData == null){
            sData = new Data();
            BaseApplication.getInstance().setPutArray(1111111,sData);
        }
        return sData;
    }

    public boolean isInquirySpouse() {
        return mInquirySpouse;
    }

    public void setInquirySpouse(boolean inquirySpouse) {
        mInquirySpouse = inquirySpouse;
    }

    public FamilyBean getMy(String s){
        for (FamilyBean sFamilyBean : mList){
            if (sFamilyBean.getMemberId().equals(s)){
                return sFamilyBean;
            }
        }
        return null;
    }

    public List<FamilyBean> getChildrenAndGrandChildren(FamilyBean parentInfo, String ignoreId) {
        final String parentId = parentInfo.getMemberId();
        final List<FamilyBean> childrenList = findChildrenByParentId(parentId, ignoreId);
        setSpouse(childrenList);
        setChildren(childrenList);

        return childrenList;
    }

    private void setSpouse(List<FamilyBean> childrenList) {
        for (FamilyBean family : childrenList) {
            setSpouse(family);
        }
    }

    public void setSpouse(FamilyBean family) {
        final String spouseId = family.getSpouseId();
        family.setSpouse(getMy(spouseId));
    }

    private List<FamilyBean> findChildrenByParentId(String parentId, String ignoreId) {
        List<FamilyBean> sFamilyBeans = new ArrayList<>();
        for (FamilyBean sFamilyBean : mList){
            if ((sFamilyBean.getMotherId().equals(parentId) || sFamilyBean.getFatherId().equals(parentId)) && !sFamilyBean.getMemberId().equals(ignoreId)) {
                sFamilyBeans.add(sFamilyBean);
            }
        }
        return sFamilyBeans;
    }

    private void setChildren(List<FamilyBean> famliyList) {
        if (famliyList != null) {
            for (FamilyBean family : famliyList) {
                final String familyId = family.getMemberId();
                final List<FamilyBean> childrenList = findChildrenByParentId(familyId, "");
                if (childrenList != null && mInquirySpouse) {
                    setSpouse(childrenList);
                }
                family.setChildren(childrenList);
            }
        }
    }

    public FamilyBean getCouple(String maleId, String femaleId) {
        final FamilyBean male = getMy(maleId);
        final FamilyBean female = getMy(femaleId);
        if (male != null) {
            male.setSpouse(female);
            return male;
        } else if (female != null) {
            return female;
        }
        return null;
    }

    public List<FamilyBean> getMyBrothers(FamilyBean myInfo, boolean isLittle) {
        final String myId = myInfo.getMemberId();
        final String myBirthday = myInfo.getBirthday();
        final String myFatherId = myInfo.getFatherId();
        final String myMotherId = myInfo.getMotherId();
        final String parentId;
        if (!TextUtils.isEmpty(myFatherId)) {
            parentId = myFatherId;
        } else {
            parentId = myMotherId;
        }

        final List<FamilyBean> brotherList = findChildrenByParentId(parentId, myId, myBirthday, isLittle);
        setSpouse(brotherList);
        setChildren(brotherList);
        return brotherList;
    }

    private List<FamilyBean> findChildrenByParentId(String parentId, String ignoreChildId, String birthday, boolean isLittle) {
        List<FamilyBean> sFamilyBeans = new ArrayList<>();

        for (FamilyBean sFamilyBean : mList) {
            if ((sFamilyBean.getFatherId().equals(parentId) || sFamilyBean.getMemberId().equals(parentId)) && !sFamilyBean.getMemberId().equals(ignoreChildId)){

                if (isLittle) {
//                    sql += " and birthday > ?";
                    if (DateAndTimeUtil.DateBiJiao(sFamilyBean.getBirthday(),birthday)){
                        sFamilyBeans.add(sFamilyBean);
                    }
                } else {
//                    sql += " and birthday <= ?";
                    if (!DateAndTimeUtil.DateBiJiao(sFamilyBean.getBirthday(),birthday)){
                        sFamilyBeans.add(sFamilyBean);
                    }
                }
            }
        }

        return sFamilyBeans;
    }
}
