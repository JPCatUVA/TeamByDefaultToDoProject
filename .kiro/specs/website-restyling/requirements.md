# Requirements Document

## Introduction

This feature introduces a complete visual design system for the TeamByDefault To-Do application. The Angular frontend currently has well-structured HTML templates with semantic class names but no CSS styling whatsoever. This spec defines a cohesive, modern styling system covering global design tokens, typography, layout, forms, interactive elements, and responsive behavior across all six views (login, register, home, task-view, subtask-view, and app-level footer).

## Glossary

- **Design_System**: The collection of CSS custom properties (design tokens), global reset styles, typography rules, and shared component patterns that establish visual consistency across the application.
- **Style_Engine**: The combination of Angular component-scoped CSS files and the global `styles.css` file that together render the visual presentation.
- **Login_Page**: The sign-in view containing email/password form fields, a submit button, error messages, and a link to the registration page.
- **Register_Page**: The account creation view containing email/password form fields with validation feedback, a submit button, error messages, and a link back to the login page.
- **Home_View**: The main task list view containing a header with title and add-task button, an add-task form, and a scrollable task list with task items.
- **Task_View**: The task detail view containing a back button, inline-editable fields (title, description, due date, status), and a subtask section with add-subtask form and subtask list.
- **Subtask_View**: The subtask detail view containing a back button and inline-editable fields (title, description, due date, status).
- **App_Footer**: The application-level footer containing the logout button, rendered on all authenticated pages.
- **Form_Field**: A labeled input group consisting of a label element, an input or textarea element, and an optional validation error message.
- **Task_Item**: A list entry in the home view displaying task title, due date, status, and a delete button.
- **Subtask_Item**: A list entry in the task view displaying subtask title, due date, status, and a delete button.
- **Status_Badge**: A visual indicator showing whether a task or subtask is "Completed" or "Pending".
- **Edit_Row**: An inline editing interface consisting of an input/textarea, a save button, and a cancel button.

## Requirements

### Requirement 1: Global Design Tokens

**User Story:** As a developer, I want a centralized set of CSS custom properties (design tokens) defining colors, spacing, typography, and border radii, so that the entire application uses a consistent visual language.

#### Acceptance Criteria

1. THE Design_System SHALL define CSS custom properties on the `:root` selector for primary color, secondary color, accent color, background color, surface color, text color, muted text color, error color, success color, and border color (10 color tokens total).
2. THE Design_System SHALL define CSS custom properties on the `:root` selector for a spacing scale with exactly four increments: small, medium, large, and extra-large.
3. THE Design_System SHALL define CSS custom properties on the `:root` selector for font family, font size base, font size small, font size large, font size heading, font weight normal, and font weight bold (7 typography tokens total).
4. THE Design_System SHALL define CSS custom properties on the `:root` selector for border radius small, border radius medium, and border radius large.
5. THE Design_System SHALL define a CSS custom property on the `:root` selector for box shadow used on elevated surfaces.
6. THE Design_System SHALL define a CSS custom property on the `:root` selector for transition duration with a value between 150 and 250 milliseconds inclusive.

### Requirement 2: Global Reset and Base Styles

**User Story:** As a user, I want consistent default styling across all browsers, so that the application looks the same regardless of my browser choice.

#### Acceptance Criteria

1. THE Style_Engine SHALL apply a `box-sizing: border-box` rule to all elements and pseudo-elements using the universal selector `*, *::before, *::after`.
2. THE Style_Engine SHALL remove default margin and padding from the body element by setting both to 0.
3. THE Style_Engine SHALL set the body font family using the font-family design token, font size using the font-size-base token, line height of 1.5, text color using the text-color token, and background color using the background-color token.
4. THE Style_Engine SHALL apply `-webkit-font-smoothing: antialiased` and `-moz-osx-font-smoothing: grayscale` to the body element.
5. THE Style_Engine SHALL set `font: inherit` on input, textarea, select, and button elements so they inherit the body font family and size.

### Requirement 3: Login Page Styling

**User Story:** As a user, I want the login page to be visually centered, clean, and inviting, so that I feel confident signing in.

#### Acceptance Criteria

1. THE Login_Page SHALL center its container both vertically and horizontally within the viewport.
2. THE Login_Page SHALL display the container as a card with a maximum width of 400 pixels, a surface background color, border radius, box shadow, and internal padding using the large spacing token.
3. THE Login_Page SHALL render the heading with the heading font size and center alignment.
4. THE Login_Page SHALL render the subtitle in muted text color below the heading with a bottom margin using the medium spacing token.
5. WHEN a form field has a validation error visible, THE Login_Page SHALL display the error message in the error color with the small font size.
6. THE Login_Page SHALL render the submit button with the primary color as background, white text, full width, border radius, and vertical padding using the medium spacing token.
7. WHEN the submit button is disabled, THE Login_Page SHALL set the button opacity to 0.6 and change the cursor to not-allowed.
8. THE Login_Page SHALL render the register link text centered below the form with the link button styled in the primary color without a background.
9. WHEN the user hovers over the submit button while it is enabled, THE Login_Page SHALL apply a CSS filter to darken the button background color by 10 percent.
10. THE Login_Page SHALL separate consecutive form fields with a bottom margin using the medium spacing token.

### Requirement 4: Register Page Styling

**User Story:** As a user, I want the registration page to match the login page design, so that the experience feels cohesive.

#### Acceptance Criteria

1. THE Register_Page SHALL center its container both vertically and horizontally within the viewport and display it as a card with the same surface background color, border radius, box shadow, padding, and maximum width as the Login_Page.
2. THE Register_Page SHALL render the heading with the heading font size and center alignment, and the subtitle in muted text color below the heading.
3. THE Register_Page SHALL render form fields and validation error messages using the same Form_Field pattern as the Login_Page, displaying field errors in the error color with the small font size.
4. THE Register_Page SHALL render the submit button with the primary color as background, white text, full width, border radius, and vertical padding.
5. WHEN the submit button is disabled, THE Register_Page SHALL reduce the button opacity and change the cursor to not-allowed.
6. WHEN the user hovers over the submit button while it is enabled, THE Register_Page SHALL darken the button background color.
7. WHEN the register form displays a form-level error message, THE Register_Page SHALL render the error in the error color with the Design_System medium spacing token as margin above the submit button.
8. THE Register_Page SHALL render the login link text centered below the form with the link button styled in the primary color without a background.

### Requirement 5: Home View Styling

**User Story:** As a user, I want my task list to be visually organized and easy to scan, so that I can quickly find and manage my tasks.

#### Acceptance Criteria

1. THE Home_View SHALL render the header as a flex row with the heading on the left and the add-task button on the right, with vertical centering.
2. THE Home_View SHALL render the add-task button with the primary color background, white text, and border radius.
3. WHEN the add-task form is visible, THE Home_View SHALL display it as a card below the header with surface background, border radius, box shadow, and padding and margin using the design token spacing scale.
4. THE Home_View SHALL render the task list without default list styling (no bullets, no padding).
5. THE Home_View SHALL render each Task_Item as a card with a surface background, border radius, box shadow, and padding, separated from adjacent items by a vertical gap equal to the medium spacing token.
6. THE Home_View SHALL display each Task_Item as a flex row with task information on the left and the delete button on the right, with vertical centering.
7. THE Home_View SHALL render the task title in bold and the due date and status in the muted text color with the small font size.
8. WHEN a task is completed, THE Home_View SHALL apply a line-through text decoration to the task title and reduce overall item opacity to a value between 0.5 and 0.6.
9. THE Home_View SHALL render the delete button with a transparent background, error color text, and border radius.
10. WHEN the user hovers over a Task_Item, THE Home_View SHALL increase the item box shadow depth beyond its resting elevation to indicate interactivity.
11. WHEN the task list is empty, THE Home_View SHALL display the empty-state message centered horizontally within the task list area in muted text color using the base font size.

### Requirement 6: Task View Styling

**User Story:** As a user, I want the task detail page to clearly present editable fields and subtasks, so that I can manage task details efficiently.

#### Acceptance Criteria

1. THE Task_View SHALL render the back button with a transparent background, no border, muted text color, and position it at the top-left of the view.
2. THE Task_View SHALL render the task detail section as a card with surface background, padding, border radius, and box shadow.
3. THE Task_View SHALL render each task field as a vertical group with the field label in small font size and muted text color above the field value, separated from adjacent field groups by medium spacing.
4. THE Task_View SHALL render the field value in the base font size with the edit button displayed inline to the right of the value using a transparent background, primary color text, and no border.
5. WHEN a field is in edit mode, THE Task_View SHALL display the Edit_Row as a flex row with the input occupying at least 70% of the row width and Save/Cancel buttons aligned to the right.
6. THE Task_View SHALL render the Status_Badge with the success color background for completed status and the muted text color background for pending status, with white text and border radius applied to both.
7. THE Task_View SHALL render the subtasks section below the task detail card with a header displayed as a flex row containing the section title on the left and the add-subtask button on the right, styled with primary color background, white text, and border radius.
8. THE Task_View SHALL render each Subtask_Item in the same card style as Task_Items in the Home_View.
9. WHEN a subtask is completed, THE Task_View SHALL apply a line-through decoration to the subtask title and reduce the Subtask_Item opacity to 0.6.
10. WHEN the add-subtask form is visible, THE Task_View SHALL display it as a card with form fields matching the global form field pattern.

### Requirement 7: Subtask View Styling

**User Story:** As a user, I want the subtask detail page to mirror the task detail layout, so that the editing experience is consistent.

#### Acceptance Criteria

1. THE Subtask_View SHALL render its container as a card with surface background, padding using the large spacing token, border radius, and box shadow matching the Task_View detail card.
2. THE Subtask_View SHALL render each field label in small font size and muted text color above the field value, separated from adjacent field groups by medium spacing, matching the Task_View field label pattern.
3. THE Subtask_View SHALL render the field value in the base font size with the edit button displayed inline to the right using a transparent background, primary color text, and no border, matching the Task_View field value pattern.
4. THE Subtask_View SHALL render the Edit_Row as a flex row with the input occupying at least 70% of the row width and Save/Cancel buttons aligned to the right, matching the Task_View edit-row pattern.
5. THE Subtask_View SHALL render the Status_Badge with the success color background for completed status and the muted text color background for pending status, with white text and border radius, matching the Task_View status badge.
6. THE Subtask_View SHALL render the back button with a transparent background, no border, and muted text color, matching the Task_View back button style.
7. WHEN an error message is present, THE Subtask_View SHALL display it in the error color with the small font size.

### Requirement 8: App Footer and Logout Button

**User Story:** As a user, I want the logout button to be accessible but unobtrusive, so that I can sign out without it distracting from my tasks.

#### Acceptance Criteria

1. THE App_Footer SHALL be fixed to the bottom of the viewport spanning the full width.
2. THE App_Footer SHALL render with a surface background color, a top border of 1px solid using the border color, and vertical padding using the spacing-sm design token.
3. THE App_Footer SHALL center the logout button horizontally within the footer.
4. THE App_Footer SHALL render the logout button with a transparent background, error color text, a 1px solid border in error color, and border-radius-sm.
5. WHEN the user hovers over the logout button, THE App_Footer SHALL fill the button background with the error color and change the text to white.
6. THE App_Footer SHALL ensure that page content is not obscured by the fixed footer by reserving bottom spacing equal to the footer's rendered height.

### Requirement 9: Responsive Layout

**User Story:** As a user, I want the application to be usable on both desktop and mobile screens, so that I can manage tasks from any device.

#### Acceptance Criteria

1. WHILE the viewport width is 768 pixels or wider, THE Style_Engine SHALL constrain content containers to a maximum width of 600 pixels centered horizontally.
2. WHILE the viewport width is less than 768 pixels, THE Style_Engine SHALL allow content containers to span full width with 16 pixels of horizontal padding on each side.
3. WHILE the viewport width is less than 768 pixels, THE Style_Engine SHALL stack the Home_View header (title and add-task button), Edit_Row layouts (input and Save/Cancel buttons), and Task_Item/Subtask_Item rows (info and delete button) vertically instead of in a horizontal flex row.
4. WHILE the viewport width is less than 768 pixels, THE Style_Engine SHALL ensure all interactive elements (buttons, inputs, list items) have a minimum height of 44 pixels.

### Requirement 10: Interactive States and Transitions

**User Story:** As a user, I want smooth visual feedback when I interact with buttons and form elements, so that the application feels polished and responsive.

#### Acceptance Criteria

1. THE Style_Engine SHALL apply a CSS transition on background-color, box-shadow, opacity, and transform properties for buttons and card elements with the transition duration design token and ease timing function.
2. WHEN the user focuses an input or textarea, THE Style_Engine SHALL display a 2px solid border in the primary color replacing the default border.
3. WHEN the user focuses a button via keyboard navigation, THE Style_Engine SHALL display a 2px solid outline in the primary color with a 2px offset.
4. THE Style_Engine SHALL ensure all color contrast ratios between text and backgrounds meet WCAG 2.1 AA minimum contrast ratio of 4.5:1 for normal text and 3:1 for large text.
