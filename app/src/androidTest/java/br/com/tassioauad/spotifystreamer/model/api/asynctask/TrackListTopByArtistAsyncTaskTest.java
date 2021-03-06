package br.com.tassioauad.spotifystreamer.model.api.asynctask;

import android.test.AndroidTestCase;

import junit.framework.TestCase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import br.com.tassioauad.spotifystreamer.model.api.ApiResultListener;
import br.com.tassioauad.spotifystreamer.model.api.exception.BadRequestException;
import br.com.tassioauad.spotifystreamer.model.entity.Artist;
import br.com.tassioauad.spotifystreamer.model.entity.Track;

public class TrackListTopByArtistAsyncTaskTest extends AndroidTestCase {

    TrackListTopByArtistAsyncTask trackListTopByArtistAsyncTask;

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testDoInBackground_ValidArtist() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        Artist artist = new Artist();
        artist.setId("2TI7qyDE0QfyOlnbtfDo7L");
        trackListTopByArtistAsyncTask = new TrackListTopByArtistAsyncTask(new ApiResultListener<List<Track>>() {
            @Override
            public void onResult(List<Track> trackList) {
                assertNotNull("Track list is null", trackList);
                assertTrue("Any track found", trackList.size() > 0);
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                fail(exception.getMessage());
                signal.countDown();
            }
        });

        trackListTopByArtistAsyncTask.execute(artist);
        signal.await();
    }

    public void testDoInBackground_InvalidArtist() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        Artist artist = new Artist();
        artist.setId("####");
        trackListTopByArtistAsyncTask = new TrackListTopByArtistAsyncTask(new ApiResultListener<List<Track>>() {
            @Override
            public void onResult(List<Track> trackList) {
                fail("Should happen an exception");
                signal.countDown();
            }

            @Override
            public void onException(Exception exception) {
                assertTrue("the exception is not a BadRequestException", exception instanceof BadRequestException);
                signal.countDown();
            }
        });

        trackListTopByArtistAsyncTask.execute(artist);
        signal.await();
    }
}