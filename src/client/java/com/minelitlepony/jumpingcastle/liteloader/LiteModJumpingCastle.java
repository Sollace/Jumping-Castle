
import java.io.File;
import com.mumfrey.liteloader.LiteMod;

public class LiteModMod implements LiteMod {
	
	@Override
	public String getName() {
		return "@NAME@";
	}
	
	@Override
	public String getVersion() {
		return "@VERSION@";
	}
	
	@Override
	public void init(File configPath) {
		
	}
	
	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
		
	}
}
