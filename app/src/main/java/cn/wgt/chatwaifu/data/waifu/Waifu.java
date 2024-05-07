package cn.wgt.chatwaifu.data.waifu;

public class Waifu {
    String id;
    String name;
    String hypnosis;//todo: 人物设定

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHypnosis() {
        return hypnosis;
    }

    public void setHypnosis(String hypnosis) {
        this.hypnosis = hypnosis;
    }
    //todo: for request to backend
    //todo: params 各种微调参数
}
