package com.kk.platform.service;

import com.kk.platform.dao.PayChannelDao;
import com.kk.platform.model.PayChannel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PayChannelService {
    @Autowired
    private PayChannelDao payChannelDao;

    public void createPayChannel(PayChannel payChannel) {
        payChannelDao.insert(payChannel);
    }

    public PayChannel getPayChannel(int id) {
        return payChannelDao.selectById(id);
    }


    public void updatePayChannel(PayChannel payChannel) {
        payChannelDao.update(payChannel);
    }

    public void updatePayChannel(int id, int status) {
        payChannelDao.updateStatusById(id, status);
    }

    public void deletePayChannel(int id) {
        payChannelDao.deleteById(id);
    }

    public List<PayChannel> getPayChannel(Set<Integer> channelIds) {
        if(channelIds.size() == 0){
            return new ArrayList<PayChannel>();
        }
        String ids = StringUtils.join(channelIds,",");
        return payChannelDao.getPayChannelList(ids);
    }
}
