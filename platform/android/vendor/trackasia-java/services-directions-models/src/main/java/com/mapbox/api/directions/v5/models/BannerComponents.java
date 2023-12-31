package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * A part of the {@link BannerText} which includes a snippet of the full banner text instruction. In
 * cases where data is available, an image url will be provided to visually include a road shield.
 * To receive this information, your request must have
 * <tt>MapboxDirections.Builder#bannerInstructions()</tt> set to true.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class BannerComponents extends DirectionsJsonObject
  implements Comparable<BannerComponents> {

  /**
   * Default. Indicates the text is part of the instructions and no other type.
   *
   * @since 3.0.0
   */
  public static final String TEXT = "text";

  /**
   * This is text that can be replaced by an imageBaseURL icon.
   *
   * @since 3.0.0
   */
  public static final String ICON = "icon";

  /**
   * This is text that can be dropped, and should be dropped if you are rendering icons.
   *
   * @since 3.0.0
   */
  public static final String DELIMITER = "delimiter";

  /**
   * Indicates the exit number for the maneuver.
   *
   * @since 3.0.0
   */
  public static final String EXIT_NUMBER = "exit-number";

  /**
   * Provides the the word for exit in the local language.
   *
   * @since 3.0.0
   */
  public static final String EXIT = "exit";

  /**
   * Indicates which lanes can be used to complete the maneuver.
   *
   * @since 3.0.0
   */
  public static final String LANE = "lane";

  /*
   * This view gives guidance through junctions and is used to complete maneuvers.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String GUIDANCE_VIEW = "guidance-view";

  /*
   * Sign Image After a Toll Gate. Used immediately after exiting a toll gate containing just the
   * overhead signboard. The preferred road (not the lane) arrow is highlighted on the signboard.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String AFTERTOLL = "aftertoll";

  /*
   * 3D City Real. Bird’s-eye artist’s rendition view of a general road intersection
   * and preferred road lane arrow. There is no overhead signage.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String CITYREAL = "cityreal";

  /*
   * Expressway Entrance. Bird’s-eye artist’s rendition view of the overhead signage
   * and preferred road lane arrow at an entrance ramp onto an expressway.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String EXPRESSWAY_ENTRANCE = "entrance";

  /*
   * Expressway Exit. Bird’s-eye artist’s rendition view of the overhead signage and
   * preferred road lane arrow at an exit ramp from an expressway.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String EXPRESSWAY_EXIT = "exit";

  /*
   * Junction View. Bird’s-eye artist’s rendition view of the overhead signage and
   * preferred road lane arrow on motorways where the road bifurcates into 2 or
   * more motorway trunk roads.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String JCT = "jct";

  /*
   * Service Area-Parking Area. Bird’s-eye artist’s rendition view of the overhead
   * signage and preferred road lane arrow at a rest area ramp where the route
   * leaves the main road.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String SAPA = "sapa";

  /*
   * SAPA Guide Map. Vertical artist’s rendition guide map of an SAPA rest area
   * showing various facilities icons such as restaurants, restrooms and parking areas.
   * The scale is approx. 1:5K.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String SAPAGUIDEMAP = "sapaguidemap";

  /*
   * Advanced 2D signboard. These are vendor enhanced detailed signboards.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String SIGNBOARD = "signboard";

  /*
   * Branched Image After Toll Gate. Bird’s-eye artist’s rendition view of the overhead
   * signage and preferred road lane arrow immediately after exiting a toll gate.
   */
  @SuppressWarnings("checkstyle:javadocvariable")
  public static final String TOLLBRANCH = "tollbranch";

  /**
   * Banner component types.
   * https://docs.mapbox.com/api/navigation/#banner-instruction-object
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef( {
          TEXT,
          ICON,
          DELIMITER,
          EXIT_NUMBER,
          EXIT,
          LANE,
          GUIDANCE_VIEW
  })
  public @interface BannerComponentsType {
  }

  /**
   * Banner component types.
   * https://docs.mapbox.com/api/navigation/#banner-instruction-object
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef( {
      AFTERTOLL,
      CITYREAL,
      EXPRESSWAY_ENTRANCE,
      EXPRESSWAY_EXIT,
      JCT,
      SAPA,
      SAPAGUIDEMAP,
      SIGNBOARD,
      TOLLBRANCH
  })
  public @interface BannerComponentsSubType {
  }

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_BannerComponents.Builder();
  }

  /**
   * A snippet of the full {@link BannerText#text()} which can be used for visually altering parts
   * of the full string.
   *
   * @return a single snippet of the full text instruction
   * @since 3.0.0
   */
  @NonNull
  public abstract String text();

  /**
   * String giving you more context about the component which may help in visual markup/display
   * choices. If the type of the components is unknown it should be treated as text.
   * <p>
   * Possible values:
   * <ul>
   * <li><strong>text (default)</strong>: indicates the text is part of
   * the instructions and no other type</li>
   * <li><strong>icon</strong>: this is text that can be replaced by an icon, see imageBaseURL</li>
   * <li><strong>delimiter</strong>: this is text that can be dropped and
   * should be dropped if you are rendering icons</li>
   * <li><strong>exit-number</strong>: the exit number for the maneuver</li>
   * <li><strong>exit</strong>: the word for exit in the local language</li>
   * </ul>
   *
   * @return String type from above list
   * @since 3.0.0
   */
  @NonNull
  @BannerComponentsType
  public abstract String type();

  /*
   * String giving you more context about {@link BannerComponentsType} which
   * may help in visual markup/display choices.
   */
  @Nullable
  @BannerComponentsSubType
  @SuppressWarnings("checkstyle:javadocmethod")
  public abstract String subType();

  /**
   * The abbreviated form of text.
   * <p>
   * If this is present, there will also be an abbr_priority value.
   *
   * @return abbreviated form of {@link BannerComponents#text()}.
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("abbr")
  public abstract String abbreviation();

  /**
   * An integer indicating the order in which the abbreviation abbr should be used in
   * place of text. The highest priority is 0 and a higher integer value indicates a lower
   * priority. There are no gaps in integer values.
   * <p>
   * Multiple components can have the same abbreviationPriority and when this happens all
   * components with the same abbr_priority should be abbreviated at the same time.
   * Finding no larger values of abbreviationPriority indicates that the string is
   * fully abbreviated.
   *
   * @return Integer indicating the order of the abbreviation
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("abbr_priority")
  public abstract Integer abbreviationPriority();

  /**
   * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
   * icon that's included to better identify to your user to roadway. Note that this doesn't
   * return the image itself but rather the url which can be used to download the file.
   *
   * @return the url which can be used to download the shield icon if one is available
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("imageBaseURL")
  public abstract String imageBaseUrl();

  /**
   * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
   * icon that's included to better identify to your user to roadway. Note that this doesn't
   * return the image itself but rather a complex object that can used to formulate the url which
   * in turn would be used to make a network request and download a file.
   *
   * @return a complex object which can be used to download the shield icon if one is available
   * @since 3.0.0
   */
  @Nullable
  @SerializedName("mapbox_shield")
  public abstract MapboxShield mapboxShield();

  /**
   * In some cases when the {@link StepManeuver} will be difficult to navigate, an image
   * can describe how to proceed. The domain name for this image is a Junction View.
   * Unlike the imageBaseUrl, this image url does not include image density encodings.
   *
   * @return the url which can be used to download the image.
   * @since 5.0.0
   */
  @Nullable
  @SerializedName("imageURL")
  public abstract String imageUrl();

  /**
   * A List of directions indicating which way you can go from a lane
   * (left, right, or straight). If the value is ['left', 'straight'],
   * the driver can go straight or left from that lane.
   * Present if this is a lane component.
   *
   * @return List of allowed directions from that lane.
   * @since 3.2.0
   */
  @Nullable
  public abstract List<String> directions();

  /**
   * A boolean telling you if that lane can be used to complete the upcoming maneuver.
   * If multiple lanes are active, then they can all be used to complete the upcoming maneuver.
   * Present if this is a lane component.
   *
   * @return List of allowed directions from that lane.
   * @since 3.2.0
   */
  @Nullable
  public abstract Boolean active();

  /**
   * When components.active is set to true, this property shows which of the lane's
   * {@link BannerComponents#directions()} is applicable to the current route, when there is
   * more than one. For example, if a lane allows you to go left or straight but your current
   * route is guiding you to the left, then this value will be set to left.
   * See {@link BannerComponents#directions()} for possible values.
   * When {@link BannerComponents#active()} is false, this property will not be included in
   * the response. Only available on the mapbox/driving profile.
   * @return applicable lane direction.
   */
  @Nullable
  @SerializedName("active_direction")
  public abstract String activeDirection();

  /**
   * Convert the current {@link BannerComponents} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link BannerComponents}.
   *
   * @return a {@link BannerComponents.Builder} with the same values set to match the ones defined
   *   in this {@link BannerComponents}
   * @since 3.1.0
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<BannerComponents> typeAdapter(Gson gson) {
    return new AutoValue_BannerComponents.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a BannerComponents
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static BannerComponents fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, BannerComponents.class);
  }

  /**
   * Allows ability to sort/compare by abbreviation priority. This is null-safe for values of
   * abbreviationPriority, and treats BannerComponents with a null abreviationPriority as having an
   * abbreviationPriority of infinity. This method returns a negative integer, zero, or a positive
   * integer as this object is less than, equal to, or greater than the specified object.
   *
   * @param bannerComponents to compare to
   * @return the compareTo int value
   * @since 3.0.0
   */
  @Override
  public int compareTo(BannerComponents bannerComponents) {
    Integer ab1 = this.abbreviationPriority();
    Integer ab2 = bannerComponents.abbreviationPriority();

    if (ab1 == null && ab2 == null) {
      return 0;
    } else if (ab1 == null) {
      return 1;
    } else if (ab2 == null) {
      return -1;
    } else {
      return ab1.compareTo(ab2);
    }
  }

  /**
   * This builder can be used to set the values describing the {@link BannerComponents}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder extends DirectionsJsonObject.Builder<Builder> {

    /**
     * A snippet of the full {@link BannerText#text()} which can be used for visually altering parts
     * of the full string.
     *
     * @param text a single snippet of the full text instruction
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder text(@NonNull String text);

    /**
     * String giving you more context about the component which may help in visual markup/display
     * choices. If the type of the components is unknown it should be treated as text.
     * <p>
     * Possible values:
     * <ul>
     * <li><strong>text (default)</strong>: indicates the text is part of the instructions
     * and no other type</li>
     * <li><strong>icon</strong>: this is text that can be replaced by an icon,
     * see imageBaseURL</li>
     * <li><strong>delimiter</strong>: this is text that can be dropped and should be dropped
     * if you are rendering icons</li>
     * <li><strong>exit-number</strong>: the exit number for the maneuver</li>
     * <li><strong>exit</strong>: the word for exit in the local language</li>
     * </ul>
     *
     * @param type String type from above list
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder type(@NonNull @BannerComponentsType String type);



    /*
     * String giving you more context about {@link BannerComponentsType}
     * which may help in visual markup/display choices.
     */
    @NonNull
    @BannerComponentsType
    @SuppressWarnings("checkstyle:javadocmethod")
    public abstract Builder subType(@Nullable @BannerComponentsSubType String subType);


    /**
     * The abbreviated form of text.
     *
     * @param abbreviation for the given text of this component
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder abbreviation(@Nullable String abbreviation);

    /**
     * An integer indicating the order in which the abbreviation abbr should be used in
     * place of text. The highest priority is 0 and a higher integer value indicates a lower
     * priority. There are no gaps in integer values.
     * <p>
     * Multiple components can have the same abbreviationPriority and when this happens all
     * components with the same abbr_priority should be abbreviated at the same time.
     * Finding no larger values of abbreviationPriority indicates that the string is
     * fully abbreviated.
     *
     * @param abbreviationPriority Integer indicating the order of the abbreviation
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder abbreviationPriority(@Nullable Integer abbreviationPriority);

    /**
     * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
     * icon that's included to better identify to your user to roadway. Note that this doesn't
     * return the image itself but rather the url which can be used to download the file.
     *
     * @param imageBaseUrl the url which can be used to download the shield icon if one is avaliable
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder imageBaseUrl(@Nullable String imageBaseUrl);

    /**
     * In some cases when the {@link LegStep} is a highway or major roadway, there might be a shield
     * icon that's included to better identify to your user to roadway. Note that this doesn't
     * return the image itself but rather a complex object that can used to formulate the url which
     * in turn would be used to make a network request and download a file.
     *
     * @param mapboxShield an object which can be used to download the shield icon if available
     * @return this builder for chaining options together
     */
    public abstract Builder mapboxShield(@Nullable MapboxShield mapboxShield);

    /**
     * In some cases when the {@link StepManeuver} will be difficult to navigate, an image
     * can describe how to proceed. The domain name for this image is a Junction View.
     * Unlike the imageBaseUrl, this image url does not include image density encodings.
     *
     * @param imageUrl the url which can be used to download the image
     * @return this builder for chaining options together
     * @since 5.0.0
     */
    public abstract Builder imageUrl(@Nullable String imageUrl);

    /**
     * A List of directions indicating which way you can go from a lane
     * (left, right, or straight). If the value is ['left', 'straight'],
     * the driver can go straight or left from that lane.
     * Present if this is a lane component.
     *
     * @param  directions List of allowed directions from that lane
     * @return this builder for chaining options together
     * @since 3.2.0
     */
    public abstract Builder directions(List<String> directions);

    /**
     * A boolean telling you if that lane can be used to complete the upcoming maneuver.
     * If multiple lanes are active, then they can all be used to complete the upcoming maneuver.
     * Present if this is a lane component.
     *
     * @param activeState true, if the lane could be used for upcoming maneuver, false - otherwise
     * @return this builder for chaining options together
     * @since 3.2.0
     */
    public abstract Builder active(Boolean activeState);

    /**
     * When components.active is set to true, this property shows which of the lane's
     * {@link BannerComponents#directions()} is applicable to the current route, when there is
     * more than one. For example, if a lane allows you to go left or straight but your current
     * route is guiding you to the left, then this value will be set to left.
     * See {@link BannerComponents#directions()} for possible values.
     * When {@link BannerComponents#active()} is false, this property will not be included in
     * the response. Only available on the mapbox/driving profile.
     *
     * @param activeDirection applicable lane direction.
     * @return this builder for chaining options together
     */
    public abstract Builder activeDirection(String activeDirection);

    /**
     * Build a new {@link BannerComponents} object.
     *
     * @return a new {@link BannerComponents} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract BannerComponents build();
  }
}
