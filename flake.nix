{
  description = "A link shortening web service";

  outputs = { self, nixpkgs }: let
      pkgs = import nixpkgs { system = "x86_64-linux"; };
    in {

    # TODO is this really needed? since we're just building a docker image,
    # maybe just convert this derivation to pkgs.buildTools.dockerImage call
    packages.x86_64-linux.sundews = pkgs.stdenv.mkDerivation {
      name = "sundews";
      version = "1.0.0";
      src = pkgs.fetchFromGitHub {
        owner = "peeley";
        repo = "sundews";
        rev = "ddbe9ffbd82b2189058e5f74c33745214d1cde14";
        sha256 = "sha256-KtLc0tc2lidRVWoObjJfeIcZOHtrUun5b8MJbTPfRYE=";
      };

      buildInputs = with pkgs; [
        leiningen
        clojure
      ];

      buildPhase = ''
        lein install
      '';
    };

    defaultPackage.x86_64-linux = self.packages.x86_64-linux.sundews;

    devShell.x86_64-linux = pkgs.mkShell {
      buildInputs = with pkgs; [
        clojure
        clj-kondo
        leiningen
        docker
      ];
    };
  };
}
