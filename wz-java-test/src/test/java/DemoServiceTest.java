import com.wz.web.domain.Demo;
import com.wz.web.domain.User;
import com.wz.web.service.DemoService;
import com.wz.web.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by wangzhen on 2016-07-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-config.xml"})
public class DemoServiceTest {
    @Resource
    private DemoService demoService;
    @Resource
    private UserService userService;

    @Test
    public void testAddUserSelective(){
        User user = new User();
        user.setUsername("笑笑");
        user.setPassword("123456");
        try {
            userService.addUserSelective(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddDemoSelective(){
        Demo demo = new Demo();
        demo.setUsername("笑笑");
        demo.setPassword("123456");
        try {
            demoService.addDemoSelective(demo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
