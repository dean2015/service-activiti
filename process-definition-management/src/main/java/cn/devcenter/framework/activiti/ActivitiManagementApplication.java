package cn.devcenter.framework.activiti;

import cn.housecenter.dlfc.framework.starter.web.webcontainer.JettyContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class ActivitiManagementApplication extends JettyContainer {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ActivitiManagementApplication.class, args);
        log.info(" ========== " + applicationContext.getId() + " started ==========");
    }

}
