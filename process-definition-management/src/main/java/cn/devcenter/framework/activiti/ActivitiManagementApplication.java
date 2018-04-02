package cn.devcenter.framework.activiti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.ApplicationContext;

@SpringCloudApplication
@Slf4j
public class ActivitiManagementApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ActivitiManagementApplication.class, args);
        log.info(" ========== " + applicationContext.getId() + " started ==========");
    }

}
