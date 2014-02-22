package de.maxgb.minecraft.second_screen;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public abstract class StandardListener {

	private String params;
	protected MinecraftServer server;
	protected int everyTick=1;
	private int tick;
	private String lastMessage;
	
	public StandardListener(String params){
		this.params=params;
		server=FMLCommonHandler.instance().getMinecraftServerInstance();
		tick=0;
	}
	
	
	/**
	 * Is called every tick
	 * @return Message which should be sent, null if none
	 */
	public String tick(){
		if(++tick>everyTick){
			tick=0;
			String newMessage=update();
			if(!newMessage.equals(lastMessage)){
				lastMessage=newMessage;
				return newMessage;
			}
		}
		return null;
	}
	
	/**
	 * Is called every {@link everyTick} tick. Should check if there are any updates which need to be sent to the client. The message is only sent if it is not the same as last time.
	 * @return Null if there is no update, otherwise a String which is sent to the client listener
	 */
	public abstract String update();
	
	/**
	 * Checks if the listener has this params
	 * @param params
	 * @return 
	 */
	public final boolean matchesParams(String params){
		return this.params==params;
	}
	

}
