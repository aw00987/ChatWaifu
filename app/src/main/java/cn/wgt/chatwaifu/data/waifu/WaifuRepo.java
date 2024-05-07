package cn.wgt.chatwaifu.data.waifu;

import java.util.List;

public interface WaifuRepo {
    List<Waifu> getHarem();

    void reload();
}
