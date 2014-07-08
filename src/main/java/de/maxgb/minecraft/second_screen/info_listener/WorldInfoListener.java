package de.maxgb.minecraft.second_screen.info_listener;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.BlockLever;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import de.maxgb.minecraft.second_screen.Configs;
import de.maxgb.minecraft.second_screen.StandardListener;
import de.maxgb.minecraft.second_screen.shared.PROTOKOLL;
import de.maxgb.minecraft.second_screen.util.Logger;
import de.maxgb.minecraft.second_screen.util.User;
import de.maxgb.minecraft.second_screen.world.ObservingManager;
import de.maxgb.minecraft.second_screen.world.ObservingManager.ObservedBlock;

public class WorldInfoListener extends StandardListener {
	/**
	 * MinecraftTime Gets a string with only needed elements. Max time is weeks
	 * 
	 * @param timeInTicks
	 * @return Time in string format
	 */
	public static String parseTime(int timeInTicks) {
		String time = "";
		//int weeks = timeInTicks / (168000);
		int remainder = timeInTicks % (168000);
		//int days = remainder / 24000;
		remainder = timeInTicks % 24000;
		int hours = remainder / 1000;
		remainder = timeInTicks % 1000;
		int minutes = remainder / 17;

		// Not interesting
		// if (weeks != 0) {
		// time += weeks + " weeks ";
		// }
		//
		// if (days != 0) {
		// time += (days < 10 ? "0" : "") + days + " days ";
		// }

		if (hours != 0) {
			time += (hours < 10 ? "0" : "") + hours + " h ";
		}

		if (minutes != 0) {
			time += (minutes < 10 ? "0" : "") + minutes + " min ";
		}

		return time;
	}

	HashMap<Integer, WorldServer> worlds;

	private final String TAG = "WorldInfoListener";

	public WorldInfoListener(User user) {
		super(user);
		everyTick = Configs.world_info_update_time;
		worlds = new HashMap<Integer, WorldServer>();

		for (WorldServer s : server.worldServers) {
			worlds.put(s.provider.dimensionId, s);
		}
		Logger.i(TAG, "Worlds: " + worlds.toString());
	}

	@Override
	public String update() {
		JSONObject info = new JSONObject();

		// General Overworldinfo
		JSONObject ow = new JSONObject();

		for (WorldServer w : server.worldServers) {
			if (w.provider.dimensionId == 0) {
				WorldInfo i = w.getWorldInfo();
				ow.put("name", i.getWorldName());
				ow.put("time", parseTime((int) i.getWorldTime() + 6000));
				ow.put("rain", i.isRaining());
				ow.put("timetillrain", parseTime(i.getRainTime()));
				break;
			}
		}
		info.put("overworld", ow);

		
		//Observing Info
		ObservingManager.addObservingInfo(info, worlds);

		// --------------------------------------------------------------------------------

		return PROTOKOLL.WORLD_INFO_LISTENER + "-" + info.toString();
	}

}
