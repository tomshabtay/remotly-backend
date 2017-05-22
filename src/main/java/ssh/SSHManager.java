package ssh;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import model.Device;

public class SSHManager {

	private static final Logger LOGGER = Logger.getLogger(SSHManager.class.getName());
	private JSch jschSSHChannel;
	private String strUserName;
	private String strConnectionIP;
	private int intConnectionPort;
	private String strPassword;
	private Session sesConnection;
	private int intTimeOut;

	private void doCommonConstructorActions(String userName, String password, String connectionIP,
			String knownHostsFileName) {
		jschSSHChannel = new JSch();

		try {
			jschSSHChannel.setKnownHosts(knownHostsFileName);
		} catch (JSchException jschX) {
			logError(jschX.getMessage());
		}

		strUserName = userName;
		strPassword = password;
		strConnectionIP = connectionIP;
	}

	public SSHManager(String userName, String password, String connectionIP, String knownHostsFileName) {
		doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
		intConnectionPort = 22;
		intTimeOut = 60000;
	}

	public SSHManager(String userName, String password, String connectionIP, String knownHostsFileName,
			int connectionPort) {
		doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
		intConnectionPort = connectionPort;
		intTimeOut = 60000;
	}

	public SSHManager(String userName, String password, String connectionIP, String knownHostsFileName,
			int connectionPort, int timeOutMilliseconds) {
		doCommonConstructorActions(userName, password, connectionIP, knownHostsFileName);
		intConnectionPort = connectionPort;
		intTimeOut = timeOutMilliseconds;
	}

	public String connect() {
		String errorMessage = null;

		try {
			sesConnection = jschSSHChannel.getSession(strUserName, strConnectionIP, intConnectionPort);
			sesConnection.setPassword(strPassword);
			// UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
			sesConnection.setConfig("StrictHostKeyChecking", "no");
			sesConnection.connect(intTimeOut);
		} catch (JSchException jschX) {
			errorMessage = jschX.getMessage();
		}

		return errorMessage;
	}

	private String logError(String errorMessage) {
		if (errorMessage != null) {
			LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
					new Object[] { strConnectionIP, intConnectionPort, errorMessage });
		}

		return errorMessage;
	}

	private String logWarning(String warnMessage) {
		if (warnMessage != null) {
			LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
					new Object[] { strConnectionIP, intConnectionPort, warnMessage });
		}

		return warnMessage;
	}

	public String sendCommand(String command) {
		StringBuilder outputBuffer = new StringBuilder();

		try {
			Channel channel = sesConnection.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			InputStream commandOutput = channel.getInputStream();
			channel.connect();
			int readByte = commandOutput.read();

			while (readByte != 0xffffffff) {
				outputBuffer.append((char) readByte);
				readByte = commandOutput.read();
			}

			channel.disconnect();
		} catch (IOException ioX) {
			logWarning(ioX.getMessage());
			return null;
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
			return null;
		}

		return outputBuffer.toString();
	}

	public String sendMoreCommands() {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String line = "";
		String[] lines = new String[5];

		try {

			Channel channel = sesConnection.openChannel("shell");
			InputStream inStream = channel.getInputStream();
			BufferedReader fromChannel = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
			OutputStream outStream = channel.getOutputStream();
			PrintWriter toChannel = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"));

			channel.connect();
			
			
			while(true){
				System.out.println(fromChannel.readLine());
				try {
					Thread.sleep(1);
					toChannel.println("ls\n");
					toChannel.flush();
					toChannel.println("cd Code\n");
					toChannel.flush();
					toChannel.println("cd ..\n");
					toChannel.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				if(1 == 0) break;
			}

			boolean ended = false;
			int i = 0;
			while (!ended) {

				lines[i % 5] = fromChannel.readLine();
				System.out.println(i % 5 + lines[i % 5]);

				if (lines[i % 5].equals("") && lines[i % 5 - 1].startsWith("Last login")) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("===== 1");
					ended = true;
				}

				i++;
			}

			toChannel.println("ls\n");
			toChannel.flush();
			ended = false;
			
			while (!ended) {

				lines[i % 5] = fromChannel.readLine();
				System.out.println(i % 5 + lines[i % 5]);

				if (lines[i % 5].endsWith("$ ") && lines[i % 5 - 1].endsWith("$ ")) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("===== 2");
					ended = true;
				}
				i++;
			}

			toChannel.println("ls");
			toChannel.flush();
			ended = false;

			while (!ended) {

				lines[i % 5] = fromChannel.readLine();
				System.out.println(i % 5 + lines[i % 5]);

				if (lines[i % 5].endsWith("$ ")) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ended = true;
				}
				i++;
			}

		} catch (JSchException | IOException jschX) {
			logWarning(jschX.getMessage());
			return null;
		}

		return line;
	}

	public void close() {
		sesConnection.disconnect();
	}

	public static String testSendCommand(Device d, String str) {
		System.out.println("sendCommand");

		/**
		 * YOU MUST CHANGE THE FOLLOWING FILE_NAME: A FILE IN THE DIRECTORY
		 * USER: LOGIN USER NAME PASSWORD: PASSWORD FOR THAT USER HOST: IP
		 * ADDRESS OF THE SSH SERVER
		 **/

		String tmp_command = "ls";
		String command = str;
		String userName = d.username;
		String password = d.password;
		String connectionIP = d.ip;
		SSHManager instance = new SSHManager(userName, password, connectionIP, "");
		String errorMessage = instance.connect();

		if (errorMessage != null) {
			System.out.println(errorMessage);
			// fail();
		}

		String expResult = "\n";
		// call sendCommand for each command and the output
		// (without prompts) is returned

		String result = instance.sendCommand("mkdir testtesttestetddd");
		instance.sendCommand("ls");

		if (str.equals(tmp_command))
			instance.close();
		// close only after all commands are sent

		return result;
		// assertEquals(expResult, result);
	}

	public static void testMultiCommands(Device d) {
		System.out.println("sendCommand");

		/**
		 * YOU MUST CHANGE THE FOLLOWING FILE_NAME: A FILE IN THE DIRECTORY
		 * USER: LOGIN USER NAME PASSWORD: PASSWORD FOR THAT USER HOST: IP
		 * ADDRESS OF THE SSH SERVER
		 **/

		String command = "ls";
		String userName = d.username;
		String password = d.password;
		String connectionIP = d.ip;
		SSHManager instance = new SSHManager(userName, password, connectionIP, "");
		String errorMessage = instance.connect();

		if (errorMessage != null) {
			System.out.println(errorMessage);
			// fail();
		}

		String expResult = "\n";
		// call sendCommand for each command and the output
		// (without prompts) is returned

		String result = instance.sendCommand("mkdir testtesttestetddd");
		instance.sendCommand("ls");

		// assertEquals(expResult, result);
	}

	private void assertEquals(String expResult, String result) {
		// TODO Auto-generated method stub

	}

	private void fail() {
		// TODO Auto-generated method stub

	}

}