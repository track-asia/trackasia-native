#pragma once

namespace mbgl {
namespace gfx {

class RendererBackend;

class BackendScope {
public:
    /// There are two types of scopes: Creating an "Implicit" scope tells TrackAsia
    /// Native that the supporting windowing system has already activated the
    /// RenderBackend and that no further actions are required. Creating an "Explicit"
    /// scope (the default) actually enables the RenderBackend, and disables it when
    /// the BackendScope is destroyed.
    enum class ScopeType : bool {
        Implicit,
        Explicit,
    };

    BackendScope(RendererBackend&, ScopeType = ScopeType::Explicit);
    ~BackendScope();

    // Returns true when there is currently a BackendScope active in this thread.
    static bool exists();

private:
    void activate();
    void deactivate();

    BackendScope* priorScope;
    BackendScope* nextScope;
    RendererBackend& backend;
    const ScopeType scopeType;
    bool activated = false;
};

} // namespace gfx
} // namespace mbgl
