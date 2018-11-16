package com.wz.statemachine;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;

/**
 * 状态机示例
 * wangzhen23
 * 2018/9/6.
 */
public class StateMachineDemo {

    public static StateMachine<States, Events> buildMachine() throws Exception {
        StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

        builder.configureStates()
                .withStates()
                .initial(States.STATE1)
                .states(EnumSet.allOf(States.class));

        builder.configureTransitions()
                .withExternal()
                .source(States.STATE1).target(States.STATE2)
                .event(Events.EVENT1)
                .and()
                .withExternal()
                .source(States.STATE2).target(States.STATE1)
                .event(Events.EVENT2);

        builder.configureConfiguration().withConfiguration().listener(new StateMachineListenerAdapter<States, Events>() {

            @Override
            public void transition(Transition<States, Events> transition) {
                if (transition.getTarget().getId() == States.STATE1) {
                    System.out.println("==================== to target state1");
                    return;
                }

                if (transition.getSource().getId() == States.STATE1
                        && transition.getTarget().getId() == States.STATE2) {
                    System.out.println("=============from state1 to state2!");
                    return;
                }
            }

        });


        return builder.build();
    }

    public static void main(String[] args) throws Exception {
        StateMachine<States, Events> stateMachine = buildMachine();
        stateMachine.start();
        stateMachine.sendEvent(Events.EVENT1);
        stateMachine.sendEvent(Events.EVENT2);
    }

    enum States {
        STATE1, STATE2
    }

    enum Events {
        EVENT1, EVENT2
    }
}
