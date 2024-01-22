package com.zun.ojbackendjudgeservice.judge.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.zun.ojapiclientsdk.model.JudgeInfo;
import com.zun.ojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.zun.ojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 执行结果是答案里的任意一个的判题策略
 * @author zunf
 */
public class AnyJudgeStrategy implements JudgeStrategy {

    @Override
    public void judge(List<String> inputList, List<String> outputList, List<String> exampleOutputList, JudgeInfo judgeInfo) {
        for (int i = 0; i < outputList.size(); i++) {
            String output = outputList.get(i);
            //规定的输出用例时，每一种答案之间用一个英文分号隔开
            String expect = exampleOutputList.get(i);
            String[] expectArr = expect.split(";");
            boolean isAccepted = false;
            for (String ex : expectArr) {
                if (StrUtil.equals(ex, output)) {
                    isAccepted = true;
                    break;
                }
            }
            if (!isAccepted) {
                StringBuilder sb = new StringBuilder();
                sb.append(JudgeInfoMessageEnum.WRONG_ANSWER.getValue());
                sb.append(", 执行输入用例：").append(inputList.get(i)).append(" 时出错");
                sb.append(", expect：").append(expect);
                sb.append(", but：").append(output);
                judgeInfo.setMessage(sb.toString());
                return;
            }
        }
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
    }
}
