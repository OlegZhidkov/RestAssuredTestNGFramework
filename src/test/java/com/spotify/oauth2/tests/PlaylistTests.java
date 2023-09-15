package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.applicationApi.PlaylistApi.post;
import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests extends BaseTest {


    @Issue("Some issue")
    @Story("User story")
    @Feature("New Feature")
    @Link("http://example.com")
    @Description("This is the test description")
    @Test(description = "should able to create a playlist")
    public void shouldBeAbleToCreatePlaylist() {

        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlaylistEqual(post(requestPlaylist).as(Playlist.class), requestPlaylist);


    }

    @Test
    public void shouldBeAbleToGetPlaylist() {

        Playlist requestPlaylist = playlistBuilder("Updated Playlist Name", "Updated playlist description", false);
        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertPlaylistEqual(post(requestPlaylist).as(Playlist.class), requestPlaylist);

    }

    @Test
    public void shouldBeAbleToUpdatePlaylist() {

        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = PlaylistApi.put(DataLoader.getInstance().getPlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
    }

    @Test
    public void shouldNotBeAbleToCreatePlaylistWithoutName() {

        Playlist requestPlaylist = playlistBuilder(null, generateDescription(), false);

        Response response = post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400);
        assertError(response.as(Error.class), StatusCode.CODE_400);
    }

    @Test
    public void shouldNotBeAbleToCreatePlaylistWithInvalidAccessToken() {

        String invalidToken = "12345";
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);
        Response response = post(invalidToken, requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401);
        assertError(response.as(Error.class), StatusCode.CODE_401);
    }

    @Step
    private Playlist playlistBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();
    }

    private void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    private void assertStatusCode(int actualStatusCode, StatusCode expectStatusCode) {
        assertThat(actualStatusCode, equalTo(expectStatusCode.code));
    }

    private void assertError(Error responseError, StatusCode expectedStatusCode) {
        assertThat(responseError.getError().getStatus(), equalTo(expectedStatusCode.code));
        assertThat(responseError.getError().getMessage(), equalTo(expectedStatusCode.errorMessage));
    }
}
