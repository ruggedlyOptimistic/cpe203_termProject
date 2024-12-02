import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class Jukebox
{
    private BasicPlayer player;
    private String songName;

    public Jukebox(String songName)
    {
        this.player = new BasicPlayer();
        this.songName = songName;
    }

    public void setSong(String newSong)
    {
        this.songName = newSong;
    }

    public String getSongName()
    {
        return songName;
    }

    public void playSong()
    {
        String pathToMp3 = System.getProperty("user.dir") + "/" + songName;

        try
        {
            player.open(new URL("file:///" + pathToMp3));
            player.play();
        }
        catch (BasicPlayerException | MalformedURLException e)
        {
            System.out.println("Cannot open the media file!");
        }
    }

    public void stopSong() throws BasicPlayerException
    {
        player.stop();
    }

    public void pauseSong() throws BasicPlayerException
    {
        try
        {
            player.pause();
        }
        catch (BasicPlayerException e)
        {
            System.out.println("Error with the pause function!");
        }

    }

    public void resumeSong()
    {
        try
        {
            player.resume();
        }
        catch (BasicPlayerException e)
        {
            System.out.println("Error with the resume function!");
        }
    }
}
