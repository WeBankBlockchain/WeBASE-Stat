/**
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package stat.test.frontInterface;

import com.webank.webase.stat.Application;
import com.webank.webase.stat.restinterface.FrontInterfaceService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FrontServiceTest {

    @Autowired
    private FrontInterfaceService frontInterface;
    private String frontIp = "127.0.0.1";
    private Integer frontPort = 5002;

    @Test
    public void getGroupListTest() {
        List<String> list = frontInterface.getGroupList(frontIp, frontPort);
        assert (list != null && list.size() > 0);
        System.out.println("=====================list:" + list);
    }
}
