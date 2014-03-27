package de.maxgb.minecraft.second_screen.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;

import de.maxgb.minecraft.second_screen.Configs;
import de.maxgb.minecraft.second_screen.util.Constants;
import de.maxgb.minecraft.second_screen.util.Logger;
import de.maxgb.minecraft.second_screen.util.User;

public class UserManager {
	private static final String TAG = "UserManager";
	private static ArrayList<User> auth_users=new ArrayList<User>();
	private static ArrayList<User> temp_users=new ArrayList<User>();
	
	public static void loadUsers(){
		auth_users=new ArrayList<User>();
		File dir=getUsersDir();
		File[] files=dir.listFiles();
		for(File f : files){
			if(f.isFile()){
				User u = loadUser(f);
				if(u!=null){
					auth_users.add(u);
				}
			}
		}
		Logger.i(TAG, "Loaded "+auth_users.size()+" users");
		
	}
	
	public static void addUser(String username,String password){
		if(username!=null&&!username.equals("")){
			for(User u:auth_users){
				if(u.username.equals(username)){
					u.setPassword(password);
					return;
				}
			}
			auth_users.add(new User(username,password));
		}

	}
	
	/**
	 * Gets the user with the given username
	 * @param username
	 * @return
	 */
	public static User getUser(String username){
		if(Configs.auth_required){
			for(User u : auth_users){
				if(u.username.equals(username)){
					return u;
				}
			}
			return null;
		}
		User u=new User(username,"");
		temp_users.add(u);
		return u;
		
	}
	
	/**
	 * Returns if the username password combination is correct
	 * @param username Username
	 * @param password Passwordhash
	 * @return
	 */
	public static boolean auth(String username,String password){
		User u = getUser(username);
		if(u==null){
			return false;
		}
		return u.getPassword().equals(password);
		
	}
	
	private static File getUsersDir(){
		File dir=new File(DataStorageDriver.getSaveDir(),Constants.USER_SAVE_DIR);
		dir.mkdirs();
		return dir;
	}
	
	/**
	 * Loads a user from file
	 * @param f
	 * @return User or null if unsuccesfull
	 */
	private static User loadUser(File f){
		Configuration config=new Configuration(f);
		
		return User.readFromConfig(config);
	}
	
	public static void saveUsers(){
		for(User u:auth_users){
			File uf=new File(getUsersDir(),u.username);
			saveUser(uf,u);
		}
	}
	private static void saveUser(File f,User u){

		try {
			f.createNewFile();
			Configuration config=new Configuration(f);
			u.saveToConfig(config);
			
		} catch (IOException e) {
			Logger.e(TAG, "Failed to create user config for "+u.username);
		}
	}
	
}
