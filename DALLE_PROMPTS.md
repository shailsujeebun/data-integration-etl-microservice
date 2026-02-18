# DALL-E Prompts - Data Integration & ETL Microservice

## Usage Notes

- Use each JSON block as the prompt specification for a single image generation.
- `Filename` and `Folder` are output mapping metadata and are intentionally outside the JSON prompt.
- Keep generated images text-free, logo-free, and watermark-free.

## GLOBAL STYLE REFERENCE

```json
{
  "style": "DATA-INTEGRATION-ETL-MICROSERVICE",
  "visual_direction": "stage source-to-transform-to-destination flow with reliability checkpoints | The first read must communicate domain and outcome in one glance | a systems architecture room with service boundaries, queues, and data stores in coherent relation",
  "composition_baseline": "center-weighted focal anchor with low-noise perimeter for CTA placement",
  "camera_baseline": "workflow sequence composition",
  "lighting_baseline": "diagram-safe contrast",
  "quality_bar": "handcrafted, contemporary, non-template visual identity",
  "output_root": "assets/images/",
  "default_restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ],
  "avoid": [
    "consumer marketing fluff",
    "startup lifestyle stock",
    "non-technical abstraction",
    "generic AI stock style",
    "plastic mannequin faces",
    "gibberish UI text"
  ]
}
```

## Output Folder Structure

```text
assets/
  images/
    diagrams/
    docs/
    flows/
    hero/
    icons/
    screenshots/
    sections/
    social/
    textures/
    ui/
```

## Asset Prompts

### 1. hero-main.png

**Filename:** `hero-main.png`
**Folder:** `assets/images/hero/`
**Dimensions:** `1920x1080`
**Priority:** `high`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Compose a signature hero scene with one dominant focal point and a clean narrative read in under three seconds. Asset intent: Primary hero image anchoring system narrative. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; The first read must communicate domain and outcome in one glance; a systems architecture room with service boundaries, queues, and data stores in coherent relation. Composition rule: center-weighted focal anchor with low-noise perimeter for CTA placement. Camera and optics: workflow sequence composition. Lighting treatment: diagram-safe contrast. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Preserve broad copy-safe negative space and stable center of interest.",
  "size": "1920x1080",
  "dimensions": "1920x1080",
  "quality": "high",
  "subject": {
    "type": "hero campaign visual",
    "intent": "Primary hero image anchoring system narrative",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "homepage hero context with copy-safe composition",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "center-weighted focal anchor with low-noise perimeter for CTA placement",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "The first read must communicate domain and outcome in one glance",
      "a systems architecture room with service boundaries, queues, and data stores in coherent relation"
    ]
  },
  "lighting": {
    "setup": "diagram-safe contrast",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "workflow sequence composition",
    "framing": "center-weighted focal anchor with low-noise perimeter for CTA placement"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Alternative pass: shift camera distance and angle while preserving narrative intent and domain realism. Keep the same narrative target (Primary hero image anchoring system narrative) and project identity for Data Integration & ETL Microservice. Re-stage using foreground subject with two depth planes and a clean top margin for navigation. Camera variant: isometric systems map. Lighting variant: clarity-first tonal range. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; The first read must communicate domain and outcome in one glance. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 2. hero-alt.png

**Filename:** `hero-alt.png`
**Folder:** `assets/images/hero/`
**Dimensions:** `1600x1000`
**Priority:** `medium`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Build a hero tableau that instantly communicates the project identity while leaving deliberate headline-safe breathing room. Asset intent: Secondary hero for responsive fallback and campaign variation. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Introduce atmosphere through depth layering, not gimmick effects; a backend operations scene showing ingestion, processing, policy, and observability cues. Composition rule: single dominant subject, left-third negative space reserved for headline overlays. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Preserve broad copy-safe negative space and stable center of interest.",
  "size": "1600x1000",
  "dimensions": "1600x1000",
  "quality": "high",
  "subject": {
    "type": "hero campaign visual",
    "intent": "Secondary hero for responsive fallback and campaign variation",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "homepage hero context with copy-safe composition",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "single dominant subject, left-third negative space reserved for headline overlays",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Introduce atmosphere through depth layering, not gimmick effects",
      "a backend operations scene showing ingestion, processing, policy, and observability cues"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "single dominant subject, left-third negative space reserved for headline overlays"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Secondary hero for responsive fallback and campaign variation) and project identity for Data Integration & ETL Microservice. Re-stage using center-weighted focal anchor with low-noise perimeter for CTA placement. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Introduce atmosphere through depth layering, not gimmick effects. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 3. feature-01.png

**Filename:** `feature-01.png`
**Folder:** `assets/images/sections/`
**Dimensions:** `1200x800`
**Priority:** `high`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Visualize the feature as a concrete user outcome, not an abstract concept render. Asset intent: Feature visual: The system shall be able to ingest data from external REST APIs (GET requests). Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Show one concrete value event tied to the named feature intent; a backend operations scene showing ingestion, processing, policy, and observability cues. Composition rule: value moment frozen mid-action, with one obvious cause and one obvious outcome. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1200x800",
  "dimensions": "1200x800",
  "quality": "high",
  "subject": {
    "type": "feature demonstration scene",
    "intent": "Feature visual: The system shall be able to ingest data from external REST APIs (GET requests)",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "feature section context supporting page narrative",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "value moment frozen mid-action, with one obvious cause and one obvious outcome",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Show one concrete value event tied to the named feature intent",
      "a backend operations scene showing ingestion, processing, policy, and observability cues"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "value moment frozen mid-action, with one obvious cause and one obvious outcome"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Feature visual: The system shall be able to ingest data from external REST APIs (GET requests).) and project identity for Data Integration & ETL Microservice. Re-stage using feature action framed at eye level with clear handoff from trigger to result. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Show one concrete value event tied to the named feature intent. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 4. feature-02.png

**Filename:** `feature-02.png`
**Folder:** `assets/images/sections/`
**Dimensions:** `1200x800`
**Priority:** `medium`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Visualize the feature as a concrete user outcome, not an abstract concept render. Asset intent: Feature visual: The system shall be able to parse CSV files from a valid local directory or S3 bucket. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Show one concrete value event tied to the named feature intent; a backend operations scene showing ingestion, processing, policy, and observability cues. Composition rule: value moment frozen mid-action, with one obvious cause and one obvious outcome. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1200x800",
  "dimensions": "1200x800",
  "quality": "high",
  "subject": {
    "type": "feature demonstration scene",
    "intent": "Feature visual: The system shall be able to parse CSV files from a valid local directory or S3 bucket",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "feature section context supporting page narrative",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "value moment frozen mid-action, with one obvious cause and one obvious outcome",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Show one concrete value event tied to the named feature intent",
      "a backend operations scene showing ingestion, processing, policy, and observability cues"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "value moment frozen mid-action, with one obvious cause and one obvious outcome"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Feature visual: The system shall be able to parse CSV files from a valid local directory or S3...) and project identity for Data Integration & ETL Microservice. Re-stage using feature action framed at eye level with clear handoff from trigger to result. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Show one concrete value event tied to the named feature intent. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 5. feature-03.png

**Filename:** `feature-03.png`
**Folder:** `assets/images/sections/`
**Dimensions:** `1200x800`
**Priority:** `medium`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Show the feature in action with clear cause-and-effect composition and no decorative filler. Asset intent: Feature visual: The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Ensure trigger and result can be inferred without text labels; a systems architecture room with service boundaries, queues, and data stores in coherent relation. Composition rule: feature action framed at eye level with clear handoff from trigger to result. Camera and optics: workflow sequence composition. Lighting treatment: diagram-safe contrast. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1200x800",
  "dimensions": "1200x800",
  "quality": "high",
  "subject": {
    "type": "feature demonstration scene",
    "intent": "Feature visual: The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "feature section context supporting page narrative",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "feature action framed at eye level with clear handoff from trigger to result",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Ensure trigger and result can be inferred without text labels",
      "a systems architecture room with service boundaries, queues, and data stores in coherent relation"
    ]
  },
  "lighting": {
    "setup": "diagram-safe contrast",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "workflow sequence composition",
    "framing": "feature action framed at eye level with clear handoff from trigger to result"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Alternative pass: shift camera distance and angle while preserving narrative intent and domain realism. Keep the same narrative target (Feature visual: The system shall be able to query external relational databases (JDBC/ODBC) to fetch new records.) and project identity for Data Integration & ETL Microservice. Re-stage using product capability shown in practical context with supporting objects in secondary focus. Camera variant: isometric systems map. Lighting variant: clarity-first tonal range. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Ensure trigger and result can be inferred without text labels. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 6. architecture-overview.png

**Filename:** `architecture-overview.png`
**Folder:** `assets/images/diagrams/`
**Dimensions:** `1600x900`
**Priority:** `high`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Express architecture depth with readable zones and implied data flow, while keeping label-free clarity. Asset intent: System architecture overview diagram image. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Represent trust boundaries and control points through layout, not captions; an engineering diagrammatic composition grounded in real workflow constraints. Composition rule: layered topology with explicit separation between actors, services, and storage. Camera and optics: interface+infrastructure hybrid framing. Lighting treatment: neutral technical light. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1600x900",
  "dimensions": "1600x900",
  "quality": "high",
  "subject": {
    "type": "system architecture visualization",
    "intent": "System architecture overview diagram image",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "technical diagram context for architecture explanation",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "layered topology with explicit separation between actors, services, and storage",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Represent trust boundaries and control points through layout, not captions",
      "an engineering diagrammatic composition grounded in real workflow constraints"
    ]
  },
  "lighting": {
    "setup": "neutral technical light",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "interface+infrastructure hybrid framing",
    "framing": "layered topology with explicit separation between actors, services, and storage"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Second pass: keep art direction fixed, change framing to test stronger mobile and card-crop behavior. Keep the same narrative target (System architecture overview diagram image) and project identity for Data Integration & ETL Microservice. Re-stage using three-zone structure (edge, core, trust boundary) with directional flow cues. Camera variant: workflow sequence composition. Lighting variant: diagram-safe contrast. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Represent trust boundaries and control points through layout, not captions. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 7. workflow-sequence.png

**Filename:** `workflow-sequence.png`
**Folder:** `assets/images/flows/`
**Dimensions:** `1600x900`
**Priority:** `high`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Visualize workflow progression with clear temporal rhythm and operational context. Asset intent: Primary user/system workflow sequence visual. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Render process logic with clean directional hierarchy; a systems architecture room with service boundaries, queues, and data stores in coherent relation. Composition rule: operational sequence composition where bottleneck and resolution are both visible. Camera and optics: workflow sequence composition. Lighting treatment: diagram-safe contrast. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1600x900",
  "dimensions": "1600x900",
  "quality": "high",
  "subject": {
    "type": "workflow sequence visualization",
    "intent": "Primary user/system workflow sequence visual",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "process visualization context for workflow communication",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "operational sequence composition where bottleneck and resolution are both visible",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Render process logic with clean directional hierarchy",
      "a systems architecture room with service boundaries, queues, and data stores in coherent relation"
    ]
  },
  "lighting": {
    "setup": "diagram-safe contrast",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "workflow sequence composition",
    "framing": "operational sequence composition where bottleneck and resolution are both visible"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Alternative pass: shift camera distance and angle while preserving narrative intent and domain realism. Keep the same narrative target (Primary user/system workflow sequence visual) and project identity for Data Integration & ETL Microservice. Re-stage using sequential handoff framing with one active step and one upcoming step visible. Camera variant: isometric systems map. Lighting variant: clarity-first tonal range. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Render process logic with clean directional hierarchy. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 8. ui-screen-01.png

**Filename:** `ui-screen-01.png`
**Folder:** `assets/images/ui/`
**Dimensions:** `1440x900`
**Priority:** `high`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Render an interface view that looks production-ready, with disciplined hierarchy and clean component rhythm. Asset intent: Main interface screenshot/mockup. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Keep states believable, with active controls clearly differentiated; an engineering diagrammatic composition grounded in real workflow constraints. Composition rule: dashboard view with intentional hierarchy between navigation, metrics, and detail. Camera and optics: interface+infrastructure hybrid framing. Lighting treatment: neutral technical light. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1440x900",
  "dimensions": "1440x900",
  "quality": "high",
  "subject": {
    "type": "application interface scene",
    "intent": "Main interface screenshot/mockup",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "application UI context with realistic density",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "dashboard view with intentional hierarchy between navigation, metrics, and detail",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Keep states believable, with active controls clearly differentiated",
      "an engineering diagrammatic composition grounded in real workflow constraints"
    ]
  },
  "lighting": {
    "setup": "neutral technical light",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "interface+infrastructure hybrid framing",
    "framing": "dashboard view with intentional hierarchy between navigation, metrics, and detail"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Second pass: keep art direction fixed, change framing to test stronger mobile and card-crop behavior. Keep the same narrative target (Main interface screenshot/mockup) and project identity for Data Integration & ETL Microservice. Re-stage using realistic UI density with one primary action path and disciplined component spacing. Camera variant: workflow sequence composition. Lighting variant: diagram-safe contrast. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Keep states believable, with active controls clearly differentiated. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 9. ui-screen-02.png

**Filename:** `ui-screen-02.png`
**Folder:** `assets/images/ui/`
**Dimensions:** `1440x900`
**Priority:** `medium`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Render an interface view that looks production-ready, with disciplined hierarchy and clean component rhythm. Asset intent: Secondary interface screenshot/mockup. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Keep states believable, with active controls clearly differentiated; an engineering diagrammatic composition grounded in real workflow constraints. Composition rule: dashboard view with intentional hierarchy between navigation, metrics, and detail. Camera and optics: interface+infrastructure hybrid framing. Lighting treatment: neutral technical light. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1440x900",
  "dimensions": "1440x900",
  "quality": "high",
  "subject": {
    "type": "application interface scene",
    "intent": "Secondary interface screenshot/mockup",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "application UI context with realistic density",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "dashboard view with intentional hierarchy between navigation, metrics, and detail",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Keep states believable, with active controls clearly differentiated",
      "an engineering diagrammatic composition grounded in real workflow constraints"
    ]
  },
  "lighting": {
    "setup": "neutral technical light",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "interface+infrastructure hybrid framing",
    "framing": "dashboard view with intentional hierarchy between navigation, metrics, and detail"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Second pass: keep art direction fixed, change framing to test stronger mobile and card-crop behavior. Keep the same narrative target (Secondary interface screenshot/mockup) and project identity for Data Integration & ETL Microservice. Re-stage using realistic UI density with one primary action path and disciplined component spacing. Camera variant: workflow sequence composition. Lighting variant: diagram-safe contrast. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Keep states believable, with active controls clearly differentiated. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 10. ops-screenshot.png

**Filename:** `ops-screenshot.png`
**Folder:** `assets/images/screenshots/`
**Dimensions:** `1600x900`
**Priority:** `medium`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Frame an operational moment that feels captured from a live, functioning system. Asset intent: Operational proof screenshot for docs. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; support knowledge retrieval cues like linked topics and navigable structure; Capture the sense of a working environment, not a sterile mock scene. Composition rule: captured-in-operation framing with contextual environment details kept subtle. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1600x900",
  "dimensions": "1600x900",
  "quality": "high",
  "subject": {
    "type": "in-product operational moment",
    "intent": "Operational proof screenshot for docs",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "operations proof context with practical realism",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "captured-in-operation framing with contextual environment details kept subtle",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "support knowledge retrieval cues like linked topics and navigable structure",
      "Capture the sense of a working environment, not a sterile mock scene"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "captured-in-operation framing with contextual environment details kept subtle"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Operational proof screenshot for docs) and project identity for Data Integration & ETL Microservice. Re-stage using live-product still with practical context but no distracting foreground clutter. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; support knowledge retrieval cues like linked topics and navigable structure. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 11. icon-set.png

**Filename:** `icon-set.png`
**Folder:** `assets/images/icons/`
**Dimensions:** `1024x1024`
**Priority:** `low`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Render icon language that feels handcrafted for this product domain, not borrowed from a generic set. Asset intent: Consistent iconography set export. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Favor semantic clarity over ornamental shape complexity; an engineering diagrammatic composition grounded in real workflow constraints. Composition rule: symbol family displayed as a coherent set, with balanced negative space. Camera and optics: interface+infrastructure hybrid framing. Lighting treatment: neutral technical light. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1024x1024",
  "dimensions": "1024x1024",
  "quality": "high",
  "subject": {
    "type": "iconography asset",
    "intent": "Consistent iconography set export",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "icon system context with consistent geometry",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "symbol family displayed as a coherent set, with balanced negative space",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Favor semantic clarity over ornamental shape complexity",
      "an engineering diagrammatic composition grounded in real workflow constraints"
    ]
  },
  "lighting": {
    "setup": "neutral technical light",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "interface+infrastructure hybrid framing",
    "framing": "symbol family displayed as a coherent set, with balanced negative space"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Second pass: keep art direction fixed, change framing to test stronger mobile and card-crop behavior. Keep the same narrative target (Consistent iconography set export) and project identity for Data Integration & ETL Microservice. Re-stage using icon sheet arranged on modular grid with strict baseline and stroke consistency. Camera variant: workflow sequence composition. Lighting variant: diagram-safe contrast. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Favor semantic clarity over ornamental shape complexity. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 12. texture-overlay.png

**Filename:** `texture-overlay.png`
**Folder:** `assets/images/textures/`
**Dimensions:** `1024x1024`
**Priority:** `low`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Generate a restrained support texture that adds depth without stealing attention from interface content. Asset intent: Subtle texture overlay tile. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Prevent obvious seams and repetitive artifacts; a backend operations scene showing ingestion, processing, policy, and observability cues. Composition rule: seamless texture field with low-contrast rhythm suitable for large backdrops. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1024x1024",
  "dimensions": "1024x1024",
  "quality": "high",
  "subject": {
    "type": "seamless texture layer",
    "intent": "Subtle texture overlay tile",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "background depth layer context for UI overlays",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "seamless texture field with low-contrast rhythm suitable for large backdrops",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Prevent obvious seams and repetitive artifacts",
      "a backend operations scene showing ingestion, processing, policy, and observability cues"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "seamless texture field with low-contrast rhythm suitable for large backdrops"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Subtle texture overlay tile) and project identity for Data Integration & ETL Microservice. Re-stage using repeat-safe atmospheric texture tuned for overlay readability. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Prevent obvious seams and repetitive artifacts. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 13. doc-cover.png

**Filename:** `doc-cover.png`
**Folder:** `assets/images/docs/`
**Dimensions:** `1400x900`
**Priority:** `low`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Craft a section support visual with clear hierarchy, clean breathing room, and domain relevance. Asset intent: Documentation cover image. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; support knowledge retrieval cues like linked topics and navigable structure; Support the adjacent copy, do not dominate it. Composition rule: support visual with one clear focal anchor and copy-safe whitespace. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1400x900",
  "dimensions": "1400x900",
  "quality": "high",
  "subject": {
    "type": "section support visual",
    "intent": "Documentation cover image",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "section support visual context aligned to the product domain",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "support visual with one clear focal anchor and copy-safe whitespace",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "support knowledge retrieval cues like linked topics and navigable structure",
      "Support the adjacent copy, do not dominate it"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "support visual with one clear focal anchor and copy-safe whitespace"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Documentation cover image) and project identity for Data Integration & ETL Microservice. Re-stage using content support scene with restrained detail and stable hierarchy. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; support knowledge retrieval cues like linked topics and navigable structure. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

### 14. og-image.png

**Filename:** `og-image.png`
**Folder:** `assets/images/social/`
**Dimensions:** `1200x630`
**Priority:** `high`

```json
{
  "model": "gpt-image-1",
  "prompt": "pipeline reliability and lineage transparency across transforms. Compose a bold social card visual with a clear focal anchor and safe zones for future copy placement. Asset intent: Open Graph social image. Scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Prioritize immediate recognizability at feed-thumbnail scale; a backend operations scene showing ingestion, processing, policy, and observability cues. Composition rule: thumbnail-first composition with immediate subject recognition at small sizes. Camera and optics: isometric systems map. Lighting treatment: clarity-first tonal range. Material and surface language: schematic texture, blueprint depth accents, controlled noise floor. Keep modular crops safe for desktop and mobile placement.",
  "size": "1200x630",
  "dimensions": "1200x630",
  "quality": "high",
  "subject": {
    "type": "social share visual",
    "intent": "Open Graph social image",
    "focus": "stage source-to-transform-to-destination flow with reliability checkpoints"
  },
  "environment": {
    "setting": "social sharing context optimized for feed thumbnails",
    "mood": "technical, clear, and trustworthy"
  },
  "composition": {
    "layout": "thumbnail-first composition with immediate subject recognition at small sizes",
    "narrative_beats": [
      "stage source-to-transform-to-destination flow with reliability checkpoints",
      "Prioritize immediate recognizability at feed-thumbnail scale",
      "a backend operations scene showing ingestion, processing, policy, and observability cues"
    ]
  },
  "lighting": {
    "setup": "clarity-first tonal range",
    "style": "DATA-INTEGRATION-ETL-MICROSERVICE"
  },
  "camera": {
    "lens": "isometric systems map",
    "framing": "thumbnail-first composition with immediate subject recognition at small sizes"
  },
  "style": {
    "aesthetic": "DATA-INTEGRATION-ETL-MICROSERVICE",
    "quality": "handcrafted, contemporary, non-template visual identity"
  },
  "variation": {
    "alternate_prompt": "Variant pass: hold the same story, but re-stage composition for a different focal rhythm and depth hierarchy. Keep the same narrative target (Open Graph social image) and project identity for Data Integration & ETL Microservice. Re-stage using campaign card layout emphasizing one memorable visual gesture. Camera variant: interface+infrastructure hybrid framing. Lighting variant: neutral technical light. Preserve these scene beats: stage source-to-transform-to-destination flow with reliability checkpoints; Prioritize immediate recognizability at feed-thumbnail scale. Hard constraints remain: no text, logos, watermark, or stock-template styling.",
    "negative_prompt": "consumer marketing fluff, startup lifestyle stock, non-technical abstraction, generic AI stock style, plastic mannequin faces, gibberish UI text, oversaturated glow wash, random symbols and nonsense glyphs"
  },
  "restrictions": [
    "no text",
    "no logos",
    "no watermarks",
    "no gibberish interface characters",
    "avoid generic stock-template style"
  ]
}
```

