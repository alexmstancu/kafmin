package org.kafmin.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.kafmin.domain.Message;
import org.kafmin.domain.MessageList;
import org.kafmin.service.MessageService;
import org.kafmin.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing {@link org.kafmin.domain.Message}.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * {@code POST  /messages} : Create a new message.
     *
     * @param message the message to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new message, or with status {@code 400 (Bad Request)} if the message has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/messages/{clusterDbId}")
    public ResponseEntity<Message> createMessage(@PathVariable Long clusterDbId, @RequestBody Message message) throws URISyntaxException, ExecutionException, InterruptedException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        messageService.produce(clusterDbId, message);
        String resultParam = message.getKey() != null ? message.getKey() : "";
        return ResponseEntity.created(new URI("/api/messages/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, resultParam))
            .body(message);
    }

    /**
     * {@code GET  /messages} : get all the messages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messages in body.
     */
    @GetMapping("/messages/{clusterDbId}/{topicName}")
    public MessageList getAllMessages(@PathVariable Long clusterDbId, @PathVariable String topicName) throws ExecutionException, InterruptedException {
        log.debug("REST request to get all Messages");
        return messageService.consume(clusterDbId, topicName);
    }

    /**
     * will not be used
     */
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        Optional<Message> message = messageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(message);
    }

    /**
     * will not be used
     */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
