package com.kk.platform.service;

import com.kk.platform.dao.PayMerchantDao;
import com.kk.platform.model.PayMerchant;
import com.kk.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayMerchantService {
    @Autowired
    private PayMerchantDao payMerchantDao;

    public String generateApiKey() {
        return MD5Util.md5(System.currentTimeMillis() + "").toLowerCase();
    }

    public void createPayMerchant(PayMerchant payMerchant) {
        payMerchantDao.insert(payMerchant);
    }

    public PayMerchant getPayMerchant(int id) {
        return payMerchantDao.selectById(id);
    }

    public PayMerchant getPayMerchant(String merchantId) {
        return payMerchantDao.selectByMerchantId(merchantId);
    }

    public void updatePayMerchant(PayMerchant payMerchant) {
        payMerchantDao.update(payMerchant);
    }

    public void updatePayMerchant(int id, int status) {
        payMerchantDao.updateStatusById(id, status);
    }

    public void deletePayMerchant(int id) {
        payMerchantDao.deleteById(id);
    }
}
