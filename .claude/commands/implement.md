# Implement

Execute the implementation based on the plan.

Usage: `/implement [task-name]`

## Steps

1. Read `.claude/tasks/[task-name]/plan.md`
2. Create TodoWrite with implementation steps from the plan
3. Execute each step:
   - Write clean, well-structured code
   - Follow project patterns (CLAUDE.md, docs/)
   - Update todos as you complete each step
4. Verify the implementation works correctly
5. Run tests if applicable

Example:
```
/implement add-expense-screen
```

This will:
- Read `.claude/tasks/add-expense-screen/plan.md`
- Implement the features according to the plan
- Update todos to track progress
