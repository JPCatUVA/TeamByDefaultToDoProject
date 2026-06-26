# Implementation Plan: Website Restyling

## Overview

This plan implements a complete CSS styling system for the Angular To-Do application. The approach is layered: first establish global design tokens and resets in `styles.css`, then style each component in its scoped CSS file, and finally add responsive breakpoints and interactive states. Each task builds incrementally so the application becomes progressively styled.

## Tasks

- [x] 1. Set up global design tokens, reset, and base styles
  - [x] 1.1 Define all CSS custom properties (design tokens) on `:root` in `angular/src/styles.css`
    - Add 10 color tokens (`--color-primary`, `--color-secondary`, `--color-accent`, `--color-bg`, `--color-surface`, `--color-text`, `--color-muted`, `--color-error`, `--color-success`, `--color-border`)
    - Add 4 spacing tokens (`--spacing-sm` 0.5rem, `--spacing-md` 1rem, `--spacing-lg` 1.5rem, `--spacing-xl` 2rem)
    - Add 7 typography tokens (`--font-family`, `--font-size-base`, `--font-size-sm`, `--font-size-lg`, `--font-size-heading`, `--font-weight-normal`, `--font-weight-bold`)
    - Add 3 radius tokens (`--radius-sm`, `--radius-md`, `--radius-lg`)
    - Add 1 shadow token (`--shadow`)
    - Add 1 transition token (`--transition-duration` 200ms)
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6_

  - [x] 1.2 Add CSS reset and base body styles in `angular/src/styles.css`
    - Apply `box-sizing: border-box` to `*, *::before, *::after`
    - Remove default margin and padding from body
    - Set body font-family, font-size, line-height 1.5, text color, and background color using tokens
    - Apply font smoothing (`-webkit-font-smoothing: antialiased`, `-moz-osx-font-smoothing: grayscale`)
    - Set `font: inherit` on input, textarea, select, and button elements
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_

- [x] 2. Style authentication pages (Login and Register)
  - [x] 2.1 Implement Login component styles in `angular/src/app/login/login.css`
    - Center `.login-container` vertically and horizontally (min-height: 100vh, flexbox centering)
    - Style container as a card (max-width 400px, surface bg, radius-md, shadow, padding spacing-lg)
    - Style heading (font-size-heading, center-aligned) and subtitle (muted color, margin-bottom spacing-md)
    - Style `.form-field` with margin-bottom spacing-md, labels in bold, inputs full-width with border and padding
    - Style `.field-error` in error color, font-size-sm
    - Style submit button: primary bg, white text, full-width, border-radius-md, padding spacing-md vertical
    - Style disabled button: opacity 0.6, cursor not-allowed
    - Style hover on enabled button: darken via CSS filter (brightness 0.9)
    - Style `.register-link` centered and `.link-btn` in primary color with no background
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9, 3.10_

  - [x] 2.2 Implement Register component styles in `angular/src/app/register/register.css`
    - Center `.register-container` using same card layout as login (max-width 400px, surface bg, radius, shadow, padding)
    - Style heading and subtitle matching login pattern
    - Style form fields, labels, inputs, and `.field-error` matching login pattern
    - Style submit button (primary bg, white text, full-width, disabled state, hover state)
    - Style `.register-error` in error color with margin-top spacing-md
    - Style `.login-link` centered and `.link-btn` in primary color with no background
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8_

- [x] 3. Checkpoint - Verify authentication pages render correctly
  - Ensure all tests pass, ask the user if questions arise.

- [x] 4. Style Home View
  - [x] 4.1 Implement Home view styles in `angular/src/app/home-view/home.css`
    - Style `.home-container` with padding using spacing tokens
    - Style `.home-header` as flex row (justify-content: space-between, align-items: center)
    - Style `.add-btn` with primary bg, white text, border-radius-md, padding
    - Style `.add-form` as a card (surface bg, radius, shadow, padding spacing-lg, margin-top spacing-md)
    - Style form fields within `.add-form` matching global form-field pattern
    - Style `.task-list` with no list style, no padding, gap between items using spacing-md
    - Style `.task-item` as a card (surface bg, radius-md, shadow, padding spacing-md) with flex row layout
    - Style `.task-info` (flex: 1) with `.task-title` in bold, `.task-due` and `.task-status` in muted/small
    - Style `.completed` class with line-through on title and reduced opacity (0.6)
    - Style `.delete-btn` with transparent bg, error color text, border-radius
    - Style `.task-item:hover` with increased shadow elevation
    - Style `.empty` message centered, muted color, base font size
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 5.10, 5.11_

- [x] 5. Style Task View
  - [x] 5.1 Implement Task View styles in `angular/src/app/task-view/task-view.css`
    - Style `.back-btn` with transparent bg, no border, muted text color, positioned at top-left
    - Style `.task-detail` as a card (surface bg, padding spacing-lg, radius-md, shadow)
    - Style `.task-field` with margin-bottom spacing-md; `.field-label` in font-size-sm, muted color
    - Style `.field-value` in base font size with `.edit-btn` inline-right (transparent bg, primary color, no border)
    - Style `.edit-row` as flex row (input flex: 1 min 70%, buttons aligned right)
    - Style `.badge-complete` (success bg, white text, radius-sm, padding) and `.badge-pending` (muted bg, white text)
    - Style `.subtasks-section` and `.subtasks-header` as flex row (title left, add-btn right)
    - Style `.subtask-item` mirroring `.task-item` card pattern from Home View
    - Style `.completed` subtask items with line-through and opacity 0.6
    - Style `.add-form` card in subtasks section matching the global form-field pattern
    - Style `.empty` message for no subtasks
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 6.10_

- [x] 6. Style Subtask View
  - [x] 6.1 Implement Subtask View styles in `angular/src/app/subtask-view/subtask-view.css`
    - Style `.subtask-view-container` with padding
    - Style `.back-btn` matching Task View back button (transparent bg, no border, muted color)
    - Style `.subtask-detail` as a card (surface bg, padding spacing-lg, radius-md, shadow)
    - Style `.subtask-field` with margin-bottom spacing-md; `.field-label` in font-size-sm, muted color
    - Style `.field-value` with `.edit-btn` inline-right (transparent bg, primary color, no border)
    - Style `.edit-row` as flex row (input flex: 1 min 70%, buttons aligned right)
    - Style `.badge-complete` and `.badge-pending` matching Task View status badges
    - Style `.error` message in error color, font-size-sm
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7_

- [x] 7. Checkpoint - Verify all main views render correctly
  - Ensure all tests pass, ask the user if questions arise.

- [x] 8. Style App Footer and add responsive layout
  - [x] 8.1 Implement App Footer styles in `angular/src/app/app.css`
    - Style `.app-footer` fixed to bottom (position: fixed, bottom: 0, width: 100%)
    - Apply surface bg, 1px solid border-top using border color, padding spacing-sm vertical
    - Center `.logout-btn` horizontally (text-align: center or flexbox)
    - Style `.logout-btn` with transparent bg, error color text, 1px solid error border, border-radius-sm
    - Style hover state: fill bg with error color, text to white
    - Add body `padding-bottom` to prevent content occlusion by the fixed footer
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6_

  - [x] 8.2 Add responsive breakpoints to all component CSS files
    - In `styles.css`: add no viewport-specific rules (tokens are universal)
    - In `home.css`: add `@media (min-width: 768px)` to constrain `.home-container` to max-width 600px centered; below 768px stack `.home-header`, `.task-item` flex layouts vertically and ensure 44px min-height on interactive elements
    - In `task-view.css`: add media query to constrain `.task-view-container` to max-width 600px centered; below 768px stack `.edit-row` vertically and ensure 44px min-height on buttons/inputs
    - In `subtask-view.css`: add media query to constrain `.subtask-view-container` to max-width 600px centered; below 768px stack `.edit-row` vertically
    - In `login.css` and `register.css`: below 768px allow container to span full width with 16px side padding; at 768px+ keep 400px max-width centered
    - _Requirements: 9.1, 9.2, 9.3, 9.4_

- [x] 9. Add interactive states and transitions
  - [x] 9.1 Add transitions and focus states across all component CSS files
    - In `styles.css`: add transition rules on buttons and cards for `background-color`, `box-shadow`, `opacity`, `transform` using `var(--transition-duration)` and `ease`
    - Add focus styles for inputs/textareas: `border: 2px solid var(--color-primary)` replacing default border
    - Add keyboard focus styles for buttons: `outline: 2px solid var(--color-primary)` with `outline-offset: 2px`
    - Verify all color contrast ratios meet WCAG 2.1 AA (4.5:1 normal text, 3:1 large text/UI)
    - _Requirements: 10.1, 10.2, 10.3, 10.4_

- [x] 10. Final checkpoint - Ensure all styles are complete and consistent
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- This feature is purely CSS — no TypeScript changes are needed
- All component CSS files already exist but are empty; implementation fills them in
- Design tokens in `styles.css` are the single source of truth for all visual values
- The design explicitly states property-based testing does not apply to this CSS-only feature
- Visual testing should be done manually or via screenshot comparison tools (Percy, Playwright)
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation between logical groups of work

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1"] },
    { "id": 1, "tasks": ["1.2"] },
    { "id": 2, "tasks": ["2.1", "2.2"] },
    { "id": 3, "tasks": ["4.1", "5.1", "6.1"] },
    { "id": 4, "tasks": ["8.1"] },
    { "id": 5, "tasks": ["8.2"] },
    { "id": 6, "tasks": ["9.1"] }
  ]
}
```
