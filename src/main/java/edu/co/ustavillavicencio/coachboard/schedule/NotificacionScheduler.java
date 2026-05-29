package edu.co.ustavillavicencio.coachboard.schedule;

import edu.co.ustavillavicencio.coachboard.entity.Inscripcion;
import edu.co.ustavillavicencio.coachboard.entity.Notificacion;
import edu.co.ustavillavicencio.coachboard.enums.EstadoInscripcion;
import edu.co.ustavillavicencio.coachboard.enums.TipoDeNotificacion;
import edu.co.ustavillavicencio.coachboard.repository.InscripcionRepository;
import edu.co.ustavillavicencio.coachboard.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionScheduler {

    private final InscripcionRepository inscripcionRepository;
    private final NotificacionRepository notificacionRepository;

    @Value("#{'${notificacion.dias-alerta:5,1}'.split(',')}")
    private List<Integer> diasAlerta;

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void procesarNotificaciones() {
        LocalDate hoy = LocalDate.now();
        log.info("Iniciando procesamiento de notificaciones - {}", hoy);
        marcarVencidas(hoy);
        notificarVencimientoProximo(hoy);
        notificarPagoPendiente();
        log.info("Procesamiento de notificaciones finalizado");
    }

    private void marcarVencidas(LocalDate hoy) {
        List<Inscripcion> vencidas = inscripcionRepository.findActivasVencidas(hoy);
        for (Inscripcion inscripcion : vencidas) {
            inscripcion.setEstado(EstadoInscripcion.VENCIDA);
            inscripcionRepository.save(inscripcion);
            if (!notificacionRepository.existsByInscripcion_IdAndTipo(
                    inscripcion.getId(), TipoDeNotificacion.MEMBRESIA_VENCIDA)) {
                guardarNotificacion(inscripcion, TipoDeNotificacion.MEMBRESIA_VENCIDA,
                        "Tu membresia \"" + inscripcion.getTipoDeMembresia().getNombre() + "\" ha vencido. Renovela para seguir disfrutando de los beneficios.", 0);
                log.info("Notificacion MEMBRESIA_VENCIDA generada para inscripcion id={}", inscripcion.getId());
            }
        }
    }

    private void notificarVencimientoProximo(LocalDate hoy) {
        for (int dias : diasAlerta) {
            LocalDate fechaObjetivo = hoy.plusDays(dias);
            List<Inscripcion> porVencer = inscripcionRepository.findActivasQueVencenEnFecha(fechaObjetivo);
            for (Inscripcion inscripcion : porVencer) {
                if (!notificacionRepository.existsByInscripcion_IdAndTipoAndDiasRestantes(
                        inscripcion.getId(), TipoDeNotificacion.VENCIMIENTO_PROXIMO, dias)) {
                    guardarNotificacion(inscripcion, TipoDeNotificacion.VENCIMIENTO_PROXIMO,
                            "Tu membresia \"" + inscripcion.getTipoDeMembresia().getNombre() + "\" vence en " + dias + " dia(s). No olvides renovarla!", dias);
                    log.info("Notificacion VENCIMIENTO_PROXIMO ({} dias) generada para inscripcion id={}", dias, inscripcion.getId());
                }
            }
        }
    }

    private void notificarPagoPendiente() {
        List<Inscripcion> pendientes = inscripcionRepository.findByEstado(EstadoInscripcion.PENDIENTE_PAGO);
        for (Inscripcion inscripcion : pendientes) {
            if (!notificacionRepository.existsByInscripcion_IdAndTipo(
                    inscripcion.getId(), TipoDeNotificacion.PAGO_PENDIENTE)) {
                guardarNotificacion(inscripcion, TipoDeNotificacion.PAGO_PENDIENTE,
                        "Tienes un pago pendiente para la membresia \"" + inscripcion.getTipoDeMembresia().getNombre() + "\". Acercate a recepcion para regularizar tu situacion.", null);
                log.info("Notificacion PAGO_PENDIENTE generada para inscripcion id={}", inscripcion.getId());
            }
        }
    }

    private void guardarNotificacion(Inscripcion inscripcion, TipoDeNotificacion tipo, String mensaje, Integer diasRestantes) {
        Notificacion notificacion = Notificacion.builder()
                .tipo(tipo)
                .mensaje(mensaje)
                .diasRestantes(diasRestantes)
                .leida(false)
                .cliente(inscripcion.getCliente())
                .inscripcion(inscripcion)
                .build();
        notificacionRepository.save(notificacion);
    }
}
