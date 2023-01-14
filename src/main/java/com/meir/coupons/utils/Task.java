package com.meir.coupons.utils;

import com.meir.coupons.logic.CouponLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class Task extends TimerTask {
    @Autowired
    private CouponLogic couponLogic;

    @PostConstruct
    public void init() {
        Timer timer = new Timer();
        timer.schedule(this, new Date(), 86400000);
    }

    @Override
    public void run() {
        couponLogic.deleteExpCoupons();
    }
}
