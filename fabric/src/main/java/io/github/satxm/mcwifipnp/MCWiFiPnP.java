package io.github.satxm.mcwifipnp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class MCWiFiPnP implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
	@Override
	public void onInitialize() {
        // 服务器生命周期管理 - 修复混入调用的空指针问题000
        ServerLifecycleEvents.SERVER_STARTING.register(Config::setCurrentServer);

        ServerLifecycleEvents.SERVER_STARTED.register(Config::setCurrentServer);

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // 注意：在服务器停止前清理，但混入可能还在调用
            // 所以我们使用延迟清理
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            Config.setCurrentServer(null);
        });

    }

	@Override
	public void onInitializeClient() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MCWiFiPnPUnit.registerCommands(dispatcher, false);
		});
	}

	@Override
	public void onInitializeServer() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MCWiFiPnPUnit.registerCommands(dispatcher, true);
		});
	}
}
