package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.WalkingOptions;
import com.mapbox.api.directions.v5.WalkingOptionsAdapterFactory;
import com.mapbox.api.directions.v5.utils.FormatUtils;
import com.mapbox.api.directions.v5.utils.ParseUtils;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.PointAsCoordinatesTypeAdapter;
import java.util.List;

/**
 * Provides information connected to your request that help when a new directions request is needing
 * using the identical parameters as the original request.
 * <p>
 * For example, if I request a driving (profile) with alternatives and continueStraight set to true.
 * I make the request but loose reference and information which built the original request. Thus, If
 * I only want to change a single variable such as the destination coordinate, i'd have to have all
 * the other route information stores so the request was made identical to the previous but only now
 * using this new destination point.
 * <p>
 * Using this class can provide you wth the information used when the {@link DirectionsRoute} was
 * made.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class RouteOptions extends DirectionsJsonObject {

  /**
   * Build a new instance of this RouteOptions class optionally settling values.
   *
   * @return {@link RouteOptions.Builder}
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_RouteOptions.Builder();
  }

  /**
   * The same base URL which was used during the request that resulted in this root directions
   * response.
   *
   * @return string value representing the base URL
   * @since 3.0.0
   */
  @NonNull
  public abstract String baseUrl();

  /**
   * The same user which was used during the request that resulted in this root directions response.
   *
   * @return string value representing the user
   * @since 3.0.0
   */
  @NonNull
  public abstract String profile();

  /**
   * A list of Points to visit in order.
   * There can be between two and 25 coordinates for most requests, or up to three coordinates for
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
   * Note that these coordinates are different than the direction responses
   * {@link DirectionsWaypoint}s that these are the non-snapped coordinates.
   *
   * @return a list of {@link Point}s which represent the route origin, destination,
   *   and optionally, waypoints
   * @since 3.0.0
   */
  @NonNull
  public abstract List<Point> coordinates();

  /**
   * The format of the returned geometry. Allowed values are:
   * {@link DirectionsCriteria#GEOMETRY_POLYLINE} (default, a polyline with a precision of five
   * decimal places), {@link DirectionsCriteria#GEOMETRY_POLYLINE6} (a polyline with a precision
   * of six decimal places).
   *
   * @return String geometry type from {@link DirectionsCriteria.GeometriesCriteria}.
   * @since 3.1.0
   */
  @Nullable
  public abstract String geometries();

  /**
   * Displays the requested type of overview geometry. Can be
   * {@link DirectionsCriteria#OVERVIEW_FULL} (the most detailed geometry
   * available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (default, a simplified version of
   * the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview geometry).
   *
   * @return null or one of the options found in {@link DirectionsCriteria.OverviewCriteria}
   * @since 3.1.0
   */
  @Nullable
  public abstract String overview();

  @Nullable
  public abstract Boolean steps();

  /**
   * A valid Mapbox access token used to making the request.
   *
   * @return a string representing the Mapbox access token
   * @since 3.0.0
   */
  @SerializedName("access_token")
  @NonNull
  public abstract String accessToken();


  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<RouteOptions> typeAdapter(Gson gson) {
    return new AutoValue_RouteOptions.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a RouteOptions
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  @NonNull
  public static RouteOptions fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    gson.registerTypeAdapterFactory(WalkingOptionsAdapterFactory.create());
    return gson.create().fromJson(json, RouteOptions.class);
  }

  /**
   * Convert the current {@link RouteOptions} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link RouteOptions}.
   *
   * @return a {@link RouteOptions.Builder} with the same values set to match the ones defined
   *   in this {@link RouteOptions}
   */
  @NonNull
  public abstract Builder toBuilder();

  /**
   * This builder can be used to set the values describing the {@link RouteOptions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The base URL that was used during the request time and resulted in this responses
     * result.
     *
     * @param baseUrl base URL used for original request
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * The routing profile to use. Possible values are
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
     * {@link DirectionsCriteria#PROFILE_DRIVING}, {@link DirectionsCriteria#PROFILE_WALKING}, or
     * {@link DirectionsCriteria#PROFILE_CYCLING}.
     * The same profile which was used during the request that resulted in this root directions
     * response. <tt>MapboxDirections.Builder</tt> ensures that a profile is always set even if the
     * <tt>MapboxDirections</tt> requesting object doesn't specifically set a profile.
     *
     * @param profile One of the direction profiles defined in
     *                {@link DirectionsCriteria.ProfileCriteria}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder profile(@NonNull @DirectionsCriteria.ProfileCriteria String profile);

    /**
     * A list of Points to visit in order.
     * There can be between two and 25 coordinates for most requests, or up to three coordinates for
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
     * Note that these coordinates are different than the direction responses
     * {@link DirectionsWaypoint}s that these are the non-snapped coordinates.
     *
     * @param coordinates a list of {@link Point}s which represent the route origin, destination,
     *                    and optionally, waypoints
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder coordinates(@NonNull List<Point> coordinates);

    /**
     * The format of the returned geometry. Allowed values are:
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE} (default, a polyline with a precision of five
     * decimal places), {@link DirectionsCriteria#GEOMETRY_POLYLINE6} (a polyline with a precision
     * of six decimal places).
     * A null value will reset this field to the APIs default value vs this SDKs default value of
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     *
     * @param geometries one of the options found in {@link DirectionsCriteria.GeometriesCriteria}.
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder geometries(
        @NonNull @DirectionsCriteria.GeometriesCriteria String geometries);

    /**
     * Displays the requested type of overview geometry. Can be
     * {@link DirectionsCriteria#OVERVIEW_FULL} (the most detailed geometry
     * available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (default, a simplified version of
     * the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview geometry).
     *
     * @param overview one of the options found in {@link DirectionsCriteria.OverviewCriteria}
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder overview(
      @NonNull @DirectionsCriteria.OverviewCriteria String overview
    );

    public abstract Builder steps(@NonNull Boolean steps);

    /**
     * A valid Mapbox access token used to making the request.
     *
     * @param accessToken a string containing a valid Mapbox access token
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Exclude certain road types from routing. The default is to not exclude anything from the
     * profile selected. The following exclude flags are available for each profile:
     *
     * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
     * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
     *
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
     * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
     * {@link DirectionsCriteria#EXCLUDE_FERRY}.
     *
     * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
     *
     * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
     *
     * @param exclude a string matching one of the {@link DirectionsCriteria.ExcludeCriteria}
     *                exclusions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder exclude(@NonNull String exclude);


    /**
     * Builds a new instance of the {@link RouteOptions} object.
     *
     * @return a new {@link RouteOptions} instance
     * @since 3.0.0
     */
    public abstract RouteOptions build();
  }
}
