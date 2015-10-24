package br.com.ablebit.clj.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import br.com.ablebit.clj.audio.jsinfo.AudioFormatPanel;

/**
 * Testa formatos suportados para entrada e saida de audio.
 * 
 * @author lfranchi
 *
 */
public class DataLineTester {

	/**
	 * Executor principal.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<AudioFormat> audioFormatList = listAllPossiblesAudioFormats();

		List<AudioFormat> supportedSourceList = new ArrayList<AudioFormat>();
		List<AudioFormat> supportedTargetList = new ArrayList<AudioFormat>();
		
		for (AudioFormat format : audioFormatList) {
		
			Line.Info sourceDataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			
			try (SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(sourceDataLineInfo)) {
			
				if (AudioSystem.isLineSupported(sourceDataLineInfo)) {
					
					if (areThereSourceDataLines(sourceDataLineInfo, format)) {

						supportedSourceList.add(format);
					
					}
				}
				
			} catch (Exception e) {
				// e.printStackTrace();
			}
			
			Line.Info targetDataLineInfo = new DataLine.Info(TargetDataLine.class, format);

			try (TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetDataLineInfo)) {
				
				if (AudioSystem.isLineSupported(targetDataLineInfo)) {
					
					if (areThereTargetDataLines(targetDataLineInfo, format)) {
				
						supportedTargetList.add(format);
					
					}

				}
				
			} catch(Exception e) {
				// e.printStackTrace();
			}

		}

		File file = new File("result.txt");

		try {
			
			FileWriter writer = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(writer);
			
			printWriter.println("SourceDataLine");
			printWriter.println("000-----------------------------------------------------------------------");
			printWriter.print(printFormats(supportedSourceList));			
			printWriter.println("000-----------------------------------------------------------------------");
			
			printWriter.println(" ");
			
			printWriter.println("TargetDataLine");
			printWriter.println("000-----------------------------------------------------------------------");
			printWriter.print(printFormats(supportedTargetList));			
			printWriter.println("000-----------------------------------------------------------------------");
			
			printWriter.flush();
			printWriter.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		
		System.out.println("Fim");
		
	}
	
	public static boolean areThereSourceDataLines(Line.Info info, AudioFormat audioFormat) {

		SourceDataLine dataline = null;
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
		    try {
		        Mixer mixer = AudioSystem.getMixer(mixerInfo);
		        dataline = (SourceDataLine)mixer.getLine(info);
		        if(dataline==null) {
		            continue; //Doesn't support this format
		        }
		        dataline.open(audioFormat);
		        dataline.start();
		    }
		    catch (Exception ex) {
		        //If we get here it's a buggered line, so loop round again
		        continue;
		    }
		    try {
		        dataline.close();
		    }
		    catch (Exception ex) {
		        ex.printStackTrace(); //Shouldn't get here
		    }
		}


		if(dataline==null) {
		    //No dataline capable of *really* playing the stream
			return false;
		} else {
		    //We have a non-lying dataline!
			return true;
		}
		
	}

	public static boolean areThereTargetDataLines(Line.Info info, AudioFormat audioFormat) {

		TargetDataLine dataline = null;
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
		    try {
		        Mixer mixer = AudioSystem.getMixer(mixerInfo);
		        dataline = (TargetDataLine)mixer.getLine(info);
		        if(dataline==null) {
		            continue; //Doesn't support this format
		        }
		        dataline.open(audioFormat);
		        dataline.start();
		    }
		    catch (Exception ex) {
		        //If we get here it's a buggered line, so loop round again
		        continue;
		    }
		    try {
		        dataline.close();
		    }
		    catch (Exception ex) {
		        ex.printStackTrace(); //Shouldn't get here
		    }
		}


		if(dataline==null) {
		    //No dataline capable of *really* playing the stream
			return false;
		} else {
		    //We have a non-lying dataline!
			return true;
		}
		
	}

	/**
	 * Monta lista com todas possibilidades de formato de audio.
	 * 
	 * @return
	 */
	public static List<AudioFormat> listAllPossiblesAudioFormats() {
		
		List<AudioFormat> audioFormatList = new ArrayList<AudioFormat>();
		
		for (AudioFormat.Encoding encodig : AudioFormatPanel.m_aEncodings) {
			
			for (String sampleRate : AudioFormatPanel.sm_aDefaultRates) {
				
				for (String frameRate : AudioFormatPanel.sm_aDefaultRates) {
				
					for (String sampleSize : AudioFormatPanel.sm_aDefaultSampleSizes) {
						
						for(String channel : AudioFormatPanel.sm_aDefaultChannels) {
							
							for (String frameSize : AudioFormatPanel.sm_aDefaultFrameSizes) {
							
								for(int bigEndian = 0; bigEndian < 2; bigEndian++) {
									
									audioFormatList.add(
									
										new AudioFormat(
												encodig,
												Float.parseFloat(sampleRate),
												Integer.parseInt(sampleSize),
												Integer.parseInt(channel),
												Integer.parseInt(frameSize),
												Float.parseFloat(frameRate),
												Boolean.parseBoolean(String.valueOf(bigEndian)))
										
										);

								}

							}

						}

					}

				}

			}

		}
		
		return audioFormatList;
		
	}
	
	public static String printFormats(List<AudioFormat> fomats) {

		StringBuilder sb = new StringBuilder();
		
		for (AudioFormat format : fomats) {
			
			sb.append("Encoding:[").append(format.getEncoding()).append("] - ");
			sb.append("SampleRate:[").append(format.getSampleRate()).append("] - ");
			sb.append("SampleSize:[").append(format.getSampleSizeInBits()).append("] - ");
			sb.append("Channels:[").append(format.getChannels()).append("] - ");
			sb.append("FrameSize:[").append(format.getFrameSize()).append("] - ");
			sb.append("FrameRate:[").append(format.getFrameRate()).append("] - ");
			sb.append("BigEndian:[").append(format.isBigEndian()).append("]").append("\n");
			
		}

		System.out.println(sb.toString());

		return sb.toString();
	}

}
