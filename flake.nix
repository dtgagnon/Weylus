{
  description = "Weylus - Use your tablet as a graphic tablet/touch screen";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    rust-overlay = {
      url = "github:oxalica/rust-overlay";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = { self, nixpkgs, flake-utils, rust-overlay }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        overlays = [ (import rust-overlay) ];
        pkgs = import nixpkgs {
          inherit system overlays;
        };

        # Define the Rust toolchain
        rustToolchain = pkgs.rust-bin.stable.latest.default.override {
          extensions = [ "rust-src" "rust-analyzer" ];
        };

        # Common build inputs for all platforms
        commonBuildInputs = with pkgs; [
          rustToolchain
          cargo
          rustc
          rust-analyzer
          pkg-config
          clang
          llvmPackages.libclang
          cmake

          # TypeScript compiler
          nodePackages.typescript

          # Build tools
          bash
          gnumake
          autoconf
          automake
          libtool
          nasm
          yasm

          # FFmpeg dependencies (for building from source)
          ffmpeg-full
          x264

          # Development tools
          git
        ];

        # Platform-specific dependencies
        linuxBuildInputs = with pkgs; [
          # X11 libraries
          xorg.libX11
          xorg.libXext
          xorg.libXrandr
          xorg.libXfixes
          xorg.libXcomposite
          xorg.libXi
          xorg.libxcb
          xorg.xcbutil

          # VA-API for hardware acceleration
          libva
          libva-utils

          # DRM libraries
          libdrm

          # GStreamer
          gstreamer
          gst-plugins-base
          gst-plugins-good
          gst-plugins-bad
          gst-plugins-ugly

          # DBus
          dbus

          # FLTK with Wayland support
          fltk
          wayland
          wayland-protocols
          libxkbcommon

          # Additional libraries
          libpng
          libjpeg
          zlib
          openssl
        ];

        # Environment variables
        commonEnvVars = {
          LIBCLANG_PATH = "${pkgs.llvmPackages.libclang.lib}/lib";
          RUST_SRC_PATH = "${rustToolchain}/lib/rustlib/src/rust/library";

          # Use system FFmpeg (optional - comment out to build from source)
          # CARGO_FEATURE_FFMPEG_SYSTEM = "1";

          # For Rust bindgen
          BINDGEN_EXTRA_CLANG_ARGS =
            "-I${pkgs.llvmPackages.libclang.lib}/lib/clang/${pkgs.llvmPackages.libclang.version}/include";
        };

        linuxEnvVars = {
          PKG_CONFIG_PATH = pkgs.lib.makeSearchPath "lib/pkgconfig" (with pkgs; [
            xorg.libX11
            xorg.libXext
            xorg.libXrandr
            xorg.libXfixes
            xorg.libXcomposite
            xorg.libXi
            xorg.libxcb
            libva
            libdrm
            gstreamer
            gst-plugins-base
            dbus
            fltk
            wayland
            wayland-protocols
            libxkbcommon
            openssl
          ]);

          LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath (with pkgs; [
            xorg.libX11
            xorg.libXext
            xorg.libXrandr
            xorg.libXfixes
            xorg.libXcomposite
            xorg.libXi
            xorg.libxcb
            libva
            libdrm
            gstreamer
            gst-plugins-base
            dbus
            fltk
            wayland
            libxkbcommon
            stdenv.cc.cc.lib
            openssl
          ]);
        };

      in
      {
        devShells.default = pkgs.mkShell ({
          name = "weylus-dev";

          buildInputs = commonBuildInputs ++
            (if pkgs.stdenv.isLinux then linuxBuildInputs else []);

          shellHook = ''
            echo "🚀 Weylus development environment"
            echo ""
            echo "Available commands:"
            echo "  cargo build          - Build the project"
            echo "  cargo run            - Run Weylus"
            echo "  cargo test           - Run tests"
            echo "  tsc                  - Compile TypeScript"
            echo ""
            echo "Rust version: $(rustc --version)"
            echo "Cargo version: $(cargo --version)"
            echo "TypeScript version: $(tsc --version)"
            echo ""

            # Set git safe directory
            git config --global --add safe.directory "$(pwd)" 2>/dev/null || true
          '';
        } // commonEnvVars // (if pkgs.stdenv.isLinux then linuxEnvVars else {}));

        # Default package (optional - for building with nix build)
        packages.default = pkgs.rustPlatform.buildRustPackage {
          pname = "weylus";
          version = "0.11.4";

          src = ./.;

          cargoLock = {
            lockFile = ./Cargo.lock;
            allowBuiltinFetchGit = true;
          };

          nativeBuildInputs = commonBuildInputs;
          buildInputs = commonBuildInputs ++
            (if pkgs.stdenv.isLinux then linuxBuildInputs else []);

          meta = with pkgs.lib; {
            description = "Use your tablet as a graphic tablet/touch screen";
            homepage = "https://github.com/H-M-H/Weylus";
            license = licenses.agpl3Plus;
            maintainers = [];
          };
        };
      }
    );
}
