package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info(seckillList.toString());
        System.out.println(seckillList.toString());
    }

    @Test
    public void getById() {
        long seckillId = 1001;
        Seckill byId = seckillService.getById(seckillId);
        System.out.println(byId.toString());
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        System.out.println(exposer.toString());
        // Exposer{秒杀状态=true, md5加密值='cada9bf24207006208998b80a1909d0b', 秒杀ID=1001, 当前时间=null, 开始时间=null, 结束=null}
    }

    @Test
    public void executeSeckill() {
        long id = 1001;
        long phone = 15317566067L;
        String md5 = "cada9bf24207006208998b80a1909d0b";
        try {
            SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
            logger.info("result={}", execution);
            System.out.println(execution);
        }catch (RepeatKillException e){
            System.out.println(e.getMessage());
        }catch (SeckillCloseException e1){
            System.out.println(e1.getMessage());
        }
        //SeckillExecution{秒杀的商品ID=1001, 秒杀状态=1, 秒杀状态信息='秒杀成功', 秒杀的商品=SuccessKilled{主键ID=1001, 手机号码=15317566066, 秒杀状态=0, 创建时间=2020-04-22T12:59:34, 秒杀的商品=com.seckill.entity.Seckill{主键ID=1001, 秒杀商品='500元秒杀iPad2', 编号=0, 开始秒杀时间=2020-03-22T00:00, 结束秒杀时间=2020-05-23T00:00, 创建时间=2020-03-22T00:00}}}
    }

    @Test
    public void exportSeckillLogic() {
        long seckillId = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            System.out.println(exposer);
            long phone = 15317566068L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
                logger.info("result={}", execution);
                System.out.println(execution);
            }catch (RepeatKillException e){
                System.out.println(e.getMessage());
            }catch (SeckillCloseException e1){
                System.out.println(e1.getMessage());
            }
        }else{
            System.out.println("秒杀未开启");
        }
    }
}