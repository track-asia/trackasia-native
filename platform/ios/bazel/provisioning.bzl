load("@build_bazel_rules_apple//apple:apple.bzl", "local_provisioning_profile")
load("@rules_xcodeproj//xcodeproj:defs.bzl", "xcode_provisioning_profile")
load("config.bzl", "APPLE_MOBILE_PROVISIONING_PROFILE_NAME", "APPLE_MOBILE_PROVISIONING_PROFILE_TEAM_ID")

def configure_device_profiles():
    local_provisioning_profile(
        name = "provisioning_profile",
        profile_name = "1c33264d-c306-41e8-85a7-e741c9d32b90",
        team_id = APPLE_MOBILE_PROVISIONING_PROFILE_TEAM_ID,
    )

    xcode_provisioning_profile(
        name = "xcode_profile",
        managed_by_xcode = True,
        provisioning_profile = ":provisioning_profile",
        visibility = ["//visibility:public"],
    )
