package com.example.sseserver.service;

import com.example.sseserver.repository.EmitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;    // 1시간

    private final EmitterRepository emitterRepository;

    @Autowired
    public NotificationService(
            EmitterRepository emitterRepository
    ) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "EventStream Created. [userId="+ userId + "]", "sse");
        return emitter;
    }

    public void sendToClient(Long userId, Object data, String eventName) {

        // 1. 클라이언트의 SseEmitter를 가져온다.
        SseEmitter emitter = emitterRepository.get(userId);

        // 2. 가져온 SseEmitter가 있다면
        if (emitter != null) {
            try {

                // 3-1. 데이터를 클라이언트에게 보낸다.
                emitter.send(
                            SseEmitter.event()
                                        .id(String.valueOf(userId))
                                        .name(eventName)
                                        .data(data)
                );
            } catch (IOException exception) {

                // 3-2. 데이터 전송 중 오류가 발생하면 Emitter를 삭제하고 에러를 완료 상태로 처리
                emitterRepository.deleteById(userId);
                emitter.completeWithError(exception);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {

        // 타임아웃 설정 (타임아웃: 클라이언트가 일정 시간 동안 서버로부터 데이터를 받지 못할 경우 발생하는 상황)
        // 타임아웃이 발생하면 브라우저에서 자동으로 서버에 재연결 요청을 보내서 해결하게 된다.
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태)
        // ==> Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(userId));

        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때)
        // ==> Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }
}
