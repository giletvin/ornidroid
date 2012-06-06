; Scripts fu pour ornidroid
; jpg compression avec Gimp : très performante

; permet de compresser tous les fichiers jpg correpondant à un pattern dans un repertoire (non récursif)
; gimp -i -b '(batch-compress "*.jpg")' -b '(gimp-quit 0)'
(define (batch-compress pattern)
  (let* ((filelist (cadr (file-glob pattern 1))))
    (while (not (null? filelist))
           (let* ((filename (car filelist))
                  (image (car (gimp-file-load RUN-NONINTERACTIVE
                                              filename filename)))
                  (drawable (car (gimp-image-get-active-layer image))))
             
            (file-jpeg-save RUN-NONINTERACTIVE image drawable filename filename 0.5 0 1 1 "" 0 1 0 0)
             (gimp-image-delete image))
           (set! filelist (cdr filelist)))))



; permet de compresser unitairement un fichier jpg avec la qualité désirée
; ex : gimp -i -b '(simple-compress "butor_etoile.jpg" 0.5)' -b '(gimp-quit 0)'
(define (simple-compress filename quality)
   (gimp-message filename)
   (let* ((image (car (gimp-file-load RUN-NONINTERACTIVE filename filename)))
          (drawable (car (gimp-image-get-active-layer image))))
     
     (file-jpeg-save RUN-NONINTERACTIVE image drawable filename filename quality 0 1 1 "" 0 1 0 0)
     (gimp-image-delete image)
))
