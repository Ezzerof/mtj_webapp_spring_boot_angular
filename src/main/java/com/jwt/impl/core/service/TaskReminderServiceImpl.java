package com.jwt.impl.core.service;

import com.jwt.impl.core.exceptions.UserNotFoundException;
import com.jwt.impl.core.persistance.entity.User;
import com.jwt.impl.core.persistance.repository.UserRepository;
import com.jwt.impl.rest.payload.request.GetTaskDto;
import com.jwt.impl.security.UserPrincipal;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskReminderServiceImpl implements TaskReminderService {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public TaskReminderServiceImpl(TaskService taskService, EmailService emailService, UserRepository userRepository) {
        this.taskService = taskService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 600000)
    public void sendTaskReminderEmails() {
        LocalDateTime dueDateTime = LocalDateTime.now().plusDays(5);
        List<GetTaskDto> taskList = taskService.getAllUserTasks();
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userPrincipal.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found."));
        String to =  user.getEmail();

        for (GetTaskDto task : taskList) {
            LocalDateTime taskDateTime = LocalDateTime.parse(task.dueDateTime());
            String dateTime = formatString(task.dueDateTime());
            if (taskDateTime.isEqual(dueDateTime) || taskDateTime.isBefore(dueDateTime)) {
                String subject = "Upcoming Task Deadline Reminder: " + task.taskName() + "\uD83D\uDCC5";
                String message = "Hi " + user.getFirstName() + ", <br><br>I hope this message finds you well! Just a friendly nudge from My Task Journal to remind you that your task <u>" + task.taskName() +
                        "</u> is coming up on its due date soon. Here are the details: <br><br><b>Task:</b> " + task.taskName() + "<br><b>Task description:</b> " + task.description() + "<br><b>Due Date:</b> " + dateTime +
                        "<br><br>We understand how busy life can get, and we're here to help you stay on top of your important tasks. If you've already completed it, great job! If not, there's still some time left to wrap things up.<br><br>" +
                        "Remember, you can always adjust the due date or task details directly in the app if your plans have changed. And if you need a little extra motivation, we're here cheering you on!<br><br>" +
                        "Thank you for trusting My Task Journal to keep your tasks in check. If you have any questions or need assistance, don't hesitate to reach out.<br><br>" +
                        "Wishing you a productive day ahead!<br><br>" +
                        "Warm regards,<br>" +
                        "The My Task Journal Team";
                try {
                    emailService.sendEmail(subject, message, to);
                } catch (MessagingException e) {
                    throw new RuntimeException("Fail to send message");
                }

            }
        }
    }

    private String formatString(String dateTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateTime);
        sb.replace(10,11," ");
        return sb.toString();
    }
}
