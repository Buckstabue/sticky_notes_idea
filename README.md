# StickyNotes Idea Plugin
Do you spend much time on refactoring that is not intended in the current task? Do you forget all the time what you really need to do and what you actually should focus on? Is your commit history messy and you cannot revert breaking changes without reverting needed code?  
Here is the solution: StickyNote plugin for IDEA! It always shows you what is your current task. The workflow is like this:
1) You get a task in a task manager(Youtrack, jira, etc.).
2) You decompose the task into microtasks(like "implement api", "add animation effects" and so on).
3) With this plugin, you add those microtasks as Sticky Notes.
4) You sort them in an order you want to execute them.
5) This plugin always shows you what is your current task, so you stay focused on it.
6) If you happen to find dirty code that you would like to refactor immediately, don't hurry! Just run the action "Add Sticky Note" with a cursor-caret under the thing. This pluging saves a description and the position of the bad code and adds a sticky note at the end of a backlog of the current project.
7) You continue working on the current desired task.
8) When it's done, you commit code and click on "Done" button. The next task from backlog appears. So you start over from the point #5.
9) When you get to refactoring tasks, you can decide whether you want to execute them within the current task. If you want to, then the advantage is that you can easily navigate to the dirty code by double clicking on it.  

Advantages:
- you will be more productive because this plugin helps you stay more focused on the current tasks
- you will get a transparent commit history because each microtask was commited separately(you don't see "implement *** api" commit message in a change of an animation). More atomic commits - easier to revert breaking code
- you will have easier code reviews, less refactoring involved
- you will easily track dirty code places and fix them whenever you want.
