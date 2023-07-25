package com.cbq.quickbot.entity.action;

import com.cbq.quickbot.entity.Poke;

/**
 * @description: 戳一戳操作
 * @author 长白崎
 * @date 2023/7/26 4:53
 * @version 1.0
 */
public class PokeOperation extends QQOperation{
    private Long qq;
    public PokeOperation(){

    }
    public PokeOperation(Long qq){
        setAction("pokeOperation");
        this.qq = qq;
    }

    public Long getQq() {
        return qq;
    }

    public static class Builder{
        private PokeOperation pokeOperation;

        public Builder(){
            pokeOperation = new PokeOperation();
            pokeOperation.setAction("pokeOperation");
        }
        public Builder qq(Long qq){
            pokeOperation.qq = qq;
            return this;
        }

        public PokeOperation build(){
            return pokeOperation;
        }
    }
}
