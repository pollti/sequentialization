package Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import Tests.Execute;
import sequentialglobalprocess.S;
import util.FileExport;

/**
 * Converts a SGP algorithm to Promela. Use convert.
 * 
 * @author Tim Pollandt
 */
public class Promelizer {
	/**
	 * Converts SGP process to Promela code.
	 * 
	 * @param sgpProcess SGP process
	 * @return formatted Promela code as string
	 */
	public String convert(S sgpProcess) {
		if (Execute.DebugInfo) {
			try {
				FileExport.writeNextFile(false, "data/result", "sgp", sgpProcess.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<Integer, S> furtherS = new HashMap<Integer, S>();
		String codePart = sgpProcess.toCode(furtherS, 1);
		String promelaCode = newProcesses(furtherS) + "active proctype ModelS() {\n   " + codePart + "\n\nLEnd:\n}";
		if (Execute.DebugInfo) {
			try {
				FileExport.writeNextFile(false, "data/result", "pml", promelaCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return promelaCode;
	}

	/**
	 * Recursive builder for additional proctypes.
	 * 
	 * @param additionalS S (and index) to be converted to code
	 * @return Promela code
	 */
	private String newProcesses(Map<Integer, S> additionalS) {
		String promelaCode = "";
		// New proctype for every entry in map.
		for (Entry<Integer, S> entry : additionalS.entrySet()) {
			// New map for recursive call. New IDs by static counter in SIndependent.
			Map<Integer, S> furtherS = new HashMap<Integer, S>();
			String codePart = entry.getValue().toCode(furtherS, 1);
			// Concatination of strimgs similar to the one in convert.
			promelaCode = newProcesses(furtherS) + "\n\nproctype S" + entry.getKey() + "() {\n   " + codePart
					+ "\n   LEnd:\n}" + promelaCode;
		}
		return promelaCode;
	}
}
