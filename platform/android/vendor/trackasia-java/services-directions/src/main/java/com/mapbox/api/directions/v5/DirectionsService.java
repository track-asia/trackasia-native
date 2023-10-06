package com.mapbox.api.directions.v5;

import com.mapbox.api.directions.v5.models.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the directions service (v5).
 *
 * @since 1.0.0
 */
public interface DirectionsService {

  /**
   * Constructs the html get call using the information passed in through the
   * {@link MapboxDirections.Builder}.
   *
   * @param userAgent           the user agent
   * @param profile             the profile directions should use
   * @param coordinates         the coordinates the route should follow
   * @param accessToken         Mapbox access token
   * @param geometries          route geometry
   * @param overview            route full, simplified, etc.
   * @param steps               define if you'd like the route steps
   * @return the {@link DirectionsResponse} in a Call wrapper
   * @since 1.0.0
   */
  @GET("route/v1/{profile}/{coordinates}.json")
  Call<DirectionsResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("geometries") String geometries,
    @Query("overview") String overview,
    @Query("steps") Boolean steps,
    @Query("key") String accessToken
  );

  /**
   * Constructs the post html call using the information passed in through the
   * {@link MapboxDirections.Builder}.
   *
   * @param userAgent           the user agent
   * @param profile             the profile directions should use
   * @param coordinates         the coordinates the route should follow
   * @param accessToken         Mapbox access token
   * @param geometries          route geometry
   * @param overview            route full, simplified, etc.
   * @param steps               define if you'd like the route steps
   *                            segments
   * @return the {@link DirectionsResponse} in a Call wrapper
   * @since 4.6.0
   */
  @FormUrlEncoded
  @POST("route/v1/{profile}.json")
  Call<DirectionsResponse> postCall(
    @Header("User-Agent") String userAgent,
    @Path("profile") String profile,
    @Field("coordinates") String coordinates,
    @Field("geometries") String geometries,
    @Field("overview") String overview,
    @Field("steps") Boolean steps,
    @Query("key") String accessToken
  );
}
