# Req

Create a requirements file for a new task.

Usage: `/req [task-name]`

## Steps

1. Create `.claude/tasks/[task-name]/` directory
2. Create `requirements.md` with a template
3. User edits the file in their editor to add requirements

The requirements file template includes:
- **Overview**: What needs to be implemented
- **Features**: Specific features and functionality
- **UI/UX**: Screen design and user interactions
- **Technical Requirements**: Any technical constraints
- **Acceptance Criteria**: How to verify completion

Example:
```
/req add-expense-screen
```

This will:
- Create `.claude/tasks/add-expense-screen/requirements.md`
- Populate it with a template
- You then edit it with your requirements

After editing requirements.md, run:
```
/explore add-expense-screen
/plan add-expense-screen
/implement add-expense-screen
```
