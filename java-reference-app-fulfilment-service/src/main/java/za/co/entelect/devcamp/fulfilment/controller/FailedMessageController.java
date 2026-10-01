package za.co.entelect.devcamp.fulfilment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.devcamp.fulfilment.services.MessageReplayService;

@RestController
@RequestMapping("/v1/failed-message")
public class FailedMessageController {

    private final MessageReplayService messageReplayService;

    public FailedMessageController(
            MessageReplayService messageReplayService) {

        this.messageReplayService = messageReplayService;
    }

    @PostMapping("/replay")
    public ResponseEntity<String> replay() {

        try {
            messageReplayService.replay("admin");
            return ResponseEntity.ok(
                    "Message replayed successfully"
            );
        } catch(Exception e)
        {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
