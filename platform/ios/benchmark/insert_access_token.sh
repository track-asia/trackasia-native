echo "Inserting TrackAsia API key..."
token_file=~/.trackasia
token_file2=~/trackasia
token="$(cat $token_file 2>/dev/null || cat $token_file2 2>/dev/null || echo $MLN_API_KEY)"
if [ "$token" ]; then
    plutil -replace MLNApiKey -string $token "$TARGET_BUILD_DIR/$INFOPLIST_PATH"
    echo "API key insertion successful"
else
    echo 'Warning: Missing API key.'
fi
