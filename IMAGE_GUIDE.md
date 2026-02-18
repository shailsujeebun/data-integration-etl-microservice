# Image Guide - Data Integration & ETL Microservice

## 1. Third Pass Structure

- Guide includes explicit save paths and concrete folder structure for `Data Integration & ETL Microservice`.
- Asset directories listed below are physically created in this project.

## 2. Art Direction Baseline

- **Narrative:** Pipeline-centric platform focused on lineage, reliability, and transform transparency.
- **Mood:** Flow diagrams and data movement visuals with structured depth.
- **Primary Hook:** Data flow hero with source-to-target traceability.

## 3. Asset Root and Save Rules

- **Asset Root:** `assets/images/`
- Save production images only inside `assets/images/<folder>/...`
- Keep filenames lowercase kebab-case with stable extensions.
- Keep source files in `assets/source/` and prompt notes in `assets/prompts/`.

## 4. Folder Structure (Created)

```text
assets/
  images/
    diagrams/
    docs/
    flows/
    hero/
    icons/
    lineage/
    mappings/
    pipelines/
    screenshots/
    sections/
    social/
    textures/
    ui/
  source/
  prompts/
```

## 5. Asset Manifest and Exact Save Paths

| File | Save Path | Purpose | Dimensions | Priority |
|---|---|---|---|---|
| hero-main.png | assets/images/hero/hero-main.png | Primary hero image anchoring system narrative | 1920x1080 | high |
| hero-alt.png | assets/images/hero/hero-alt.png | Secondary hero for responsive fallback and campaign variation | 1600x1000 | medium |
| feature-01.png | assets/images/sections/feature-01.png | Feature visual: The system shall be able to ingest data from external REST APIs (GET requests). | 1200x800 | high |
| feature-02.png | assets/images/sections/feature-02.png | Feature visual: The system shall be able to parse CSV files from a valid local directory or S3 bucket. | 1200x800 | medium |
| feature-03.png | assets/images/sections/feature-03.png | Feature visual: The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records. | 1200x800 | medium |
| architecture-overview.png | assets/images/diagrams/architecture-overview.png | System architecture overview diagram image | 1600x900 | high |
| workflow-sequence.png | assets/images/flows/workflow-sequence.png | Primary user/system workflow sequence visual | 1600x900 | high |
| ui-screen-01.png | assets/images/ui/ui-screen-01.png | Main interface screenshot/mockup | 1440x900 | high |
| ui-screen-02.png | assets/images/ui/ui-screen-02.png | Secondary interface screenshot/mockup | 1440x900 | medium |
| ops-screenshot.png | assets/images/screenshots/ops-screenshot.png | Operational proof screenshot for docs | 1600x900 | medium |
| icon-set.png | assets/images/icons/icon-set.png | Consistent iconography set export | 1024x1024 | low |
| texture-overlay.png | assets/images/textures/texture-overlay.png | Subtle texture overlay tile | 1024x1024 | low |
| doc-cover.png | assets/images/docs/doc-cover.png | Documentation cover image | 1400x900 | low |
| og-image.png | assets/images/social/og-image.png | Open Graph social image | 1200x630 | high |

## 6. Palette Reference

| Token | Hex | Usage |
|---|---|---|
| Carbon | #111827 | Background/accent/highlight control |
| Steel | #374151 | Background/accent/highlight control |
| Frost | #F3F4F6 | Background/accent/highlight control |
| Mint | #34D399 | Background/accent/highlight control |
| Coral | #FB7185 | Background/accent/highlight control |

## 7. Compression and Format

- Hero and architecture visuals: AVIF/WebP <= 350KB.
- Section and UI images: WebP <= 180KB.
- Textures/icons: PNG/WebP <= 120KB.
- Social image: 1200x630 <= 300KB.

## 8. Accessibility and Contrast

- Maintain 4.5:1 minimum text contrast on image overlays.
- Provide intent-based alt text for all content images.
- Avoid relying on color only for severity or status communication.

## 9. DALL-E Mapping

- Each asset has a structured prompt and exact output path in `DALLE_PROMPTS.md`.
- Move any incorrectly generated file to the exact manifest save path.

