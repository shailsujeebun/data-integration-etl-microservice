# Design Guide - Data Integration & ETL Microservice

## 1. Design Intent

- **Concept:** Pipeline-centric platform focused on lineage, reliability, and transform transparency.
- **Interaction Principle:** Pipeline run states and retries are visible with explicit failure diagnostics.
- **Visual Mood:** Flow diagrams and data movement visuals with structured depth.
- **Hero Composition:** Data flow hero with source-to-target traceability.

## 2. Typography

- **Headlines:** Space Grotesk
- **Body/UI:** Source Sans 3
- **Code/Data:** IBM Plex Mono
- Use fluid sizing (`clamp`) for display text and dashboard/stat cards.
- Keep long-form measure between 55 and 75 characters.

## 3. Color Tokens

| Token | Hex | Role |
|---|---|---|
| Carbon | #111827 | Core interface and illustration balance |
| Steel | #374151 | Core interface and illustration balance |
| Frost | #F3F4F6 | Core interface and illustration balance |
| Mint | #34D399 | Core interface and illustration balance |
| Coral | #FB7185 | Core interface and illustration balance |

## 4. Layout and Components

- Default grid: 12 columns desktop, 8 tablet, 4 mobile.
- Use persistent side navigation for operator-heavy contexts.
- Keep card and table systems tokenized (spacing/radius/border/elevation).
- Forms must include label, helper, error, and success states.

## 5. Motion and Feedback

- Micro-interactions: 120-180ms for status and action feedback.
- Section transitions: 280-420ms with reduced-motion alternatives.
- Prioritize motion for orientation and system state updates.
- Avoid decorative loops that compete with operational content.

## 6. Diagram and Documentation Visual Language

- Architecture diagrams should use consistent node and edge style tokens.
- Sequence and flow visuals must emphasize directionality and ownership.
- Export diagrams in high-contrast variants for light and dark presentation contexts.

## 7. Accessibility

- Maintain WCAG AA contrast targets across UI and overlaid imagery.
- Ensure keyboard access for all controls and data views.
- Do not encode severity/state only by color; add labels/icons.
- Target touch area of at least 44x44 CSS pixels on mobile.

## 8. Distinctiveness Checklist

- Hero and primary imagery reflect this project domain, not generic startup patterns.
- Dashboard/console layouts map to actual workflow priorities.
- Visual language remains consistent between UI, docs, and diagrams.

