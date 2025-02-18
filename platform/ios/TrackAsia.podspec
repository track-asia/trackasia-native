Pod::Spec.new do |s|
    version = "#{ENV['VERSION']}"

    s.name = 'TrackAsia'
    s.version = version
    s.license = { :type => 'BSD', :file => "LICENSE.md" }
    s.homepage = 'https://trackasia.org/'
    s.authors = { 'TrackAsia' => '' }
    s.summary = 'Open source vector map solution for iOS with full styling capabilities.'
    s.platform = :ios
    s.source = {
        :http => "https://github.com/trackasia/trackasia-native/releases/download/ios-v#{version.to_s}/TrackAsia.dynamic.xcframework.zip",
        :type => "zip"
    }
    s.social_media_url  = 'https://mastodon.social/@trackasia'
    s.ios.deployment_target = '12.0'
    s.ios.vendored_frameworks = "TrackAsia.xcframework"
end
