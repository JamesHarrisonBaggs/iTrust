package edu.ncsu.csc.itrust.controller.obgyn;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.obgyn.Ultrasound;
import edu.ncsu.csc.itrust.model.obgyn.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "ultrasound_controller")
public class UltrasoundController extends iTrustController {

	private long patientId;
	private LocalDateTime visitDate;
	private long visitId;
	private int fetusId; 
	private int crl;
	private int bpd;
	private int hc;
	private int fl;
	private int ofd;
	private int ac;
	private int hl;
	private double efw;
	private InputStream file;
	
	private Ultrasound us;
	private UltrasoundMySQL sql;
	private SessionUtils sessionUtils;
	private boolean eligible;
	private boolean obgyn;
	private ObstetricsInitController obc;

	private Part uploadedFile;
	private boolean isFileUploaded;

	public UltrasoundController() throws DBException {
		super();
		sessionUtils = getSessionUtils();
		sql = new UltrasoundMySQL();
		obc = new ObstetricsInitController();
		setIsFileUploaded(false);
		String fetusId1 = sessionUtils.getRequestParameter("fetusID");
		if (fetusId1 != null) {
			fetusId = Integer.parseInt(fetusId1);
			visitId = getOfficeVisit().getVisitID();
			String action = sessionUtils.getRequestParameter("action");
			if (visitId != 0 && fetusId != 0 && (action.equals("view") || action.equals("add"))) {
				us = getUltrasoundVisitIDFetus();
				us.setVisitId(getOfficeVisit().getVisitID());
				us.setVisitDate(getOfficeVisit().getDate());

				try {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", us.getVisitId());
				} catch (NullPointerException e) {
					// Do nothing
				}
				visitId = us.getVisitId();
				patientId = us.getPatientId();
				if (patientId == 0) {
					patientId = Long.parseLong(
							(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
				}
				crl = us.getCrownRumpLength();
				bpd = us.getBiparietalDiameter();
				hc = us.getHeadCircumference();
				fl = us.getFemurLength();
				ofd = us.getOccipitofrontalDiameter();
				ac = us.getAbdominalCircumference();
				hl = us.getHumerusLength();
				efw = us.getEstimatedFetalWeight();
				fetusId = us.getFetusId();
				file = us.getUploadFile();
				if (file != null){
					setIsFileUploaded(true);
				}
				setObgyn();
			} else if ((action.equals("view") || action.equals("add"))){
				us = new Ultrasound();
				setObgyn();
			} else {
				removeUltrasound(visitId, fetusId);
				setObgyn();
			}
		} else {
			us = new Ultrasound();
			setObgyn();
		}
	}
	
	public void upload() throws IOException{
		if (uploadedFile != null){
			FacesContext.getCurrentInstance().addMessage("ultrasound_imgSuccess", new FacesMessage("Image Updated Successfully"));
			setIsFileUploaded(true);
			if(uploadedFile.getSubmittedFileName().contains(".pdf")){
				PDDocument document = PDDocument.load(uploadedFile.getInputStream());
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				for (int page = 0; page < document.getNumberOfPages(); ++page)
				{ 
				    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
				    // suffix in filename will be used as the file format
				    ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    ImageIO.write(bim, "png", baos);
				    InputStream is = new ByteArrayInputStream(baos.toByteArray());
				    us.setUploadFile(is);
				}
				document.close();
			} else {
				us.setUploadFile(uploadedFile.getInputStream());
			}
		}
		logTransaction(TransactionType.EDIT_ULTRASOUND, String.valueOf(visitId));
	}

	private void removeUltrasound(long visitId2, int fetusId2) throws DBException {
		sql.removeUltrasound(visitId2,fetusId2);
		
	}

	public UltrasoundController(SessionUtils sessionUtils, TransactionLogger logger) {
		super(sessionUtils, logger, null);
	}

	public OfficeVisit getOfficeVisit() throws DBException {
		long id = sessionUtils.getCurrentOfficeVisitId();
		return new OfficeVisitController().getVisitByID(id);
	}
	
	public Ultrasound getUltrasoundVisitIDFetus() throws DBException {
		long id = sessionUtils.getCurrentOfficeVisitId();
		String tmp = sessionUtils.getRequestParameter("fetusID");
		int fetusId = Integer.parseInt(tmp);
		return sql.getByVisitFetus(id, fetusId);
	}
	public List<Ultrasound> getUltrasoundsForCurrentPatientVisitID() throws DBException {
		long vid = sessionUtils.getCurrentOfficeVisitId();
		long pid = sessionUtils.getCurrentPatientMIDLong();
		return sql.getByPatientIdVisitId(pid, vid);
	}
	
	public void submit() throws DBException, IOException {
		us.setPatientId(sessionUtils.getCurrentPatientMIDLong());
		us.setVisitDate(getOfficeVisit().getDate());
		us.setVisitId(sessionUtils.getCurrentOfficeVisitId());
		us.setCrownRumpLength(crl);
		us.setBiparietalDiameter(bpd);
		us.setHeadCircumference(hc);
		us.setFemurLength(fl);
		us.setOccipitofrontalDiameter(ofd);
		us.setAbdominalCircumference(ac);
		us.setHumerusLength(hl);
		us.setEstimatedFetalWeight(efw);
		us.setFetusId(fetusId);
		setPatientId(us.getPatientId());
		setVisitId(us.getVisitId());
		upload();
		update(us);
	}
	 
	public void deleteImage() throws DBException{
		sql.removeUltrasoundImage(visitId, fetusId);
	}
	
	public void update(Ultrasound bean) throws DBException {
		try {
			sql.update(bean);
			FacesContext.getCurrentInstance().addMessage("ultrasound_formSuccess", new FacesMessage("Ultrasound Updated Successfully"));
		} catch (FormValidationException e) {
			FacesContext.getCurrentInstance().addMessage("ultrasound_formError", new FacesMessage(e.getMessage()));
		}
	}
	
	public List<Ultrasound> getUltrasoundsForCurrentPatient() throws DBException {
		long id = sessionUtils.getCurrentPatientMIDLong();
		return sql.getByPatientId(id);
	}
	
	public long getPatientId() {
		return patientId;
	}

	public LocalDateTime getVisitDate() {
		return visitDate;
	}

	public long getVisitId() {
		return visitId;
	}

	public int getFetusId() {
		return fetusId;
	}

	public int getCrl() {
		return crl;
	}

	public int getBpd() {
		return bpd;
	}

	public int getHc() {
		return hc;
	}

	public int getFl() {
		return fl;
	}

	public int getOfd() {
		return ofd;
	}

	public int getAc() {
		return ac;
	}

	public int getHl() {
		return hl;
	}

	public double getEfw() {
		return efw;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}

	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}

	public void setFetusId(int fetusId) {
		this.fetusId = fetusId;
	}

	public void setCrl(int crl) {
		this.crl = crl;
	}

	public void setBpd(int bpd) {
		this.bpd = bpd;
	}

	public void setHc(int hc) {
		this.hc = hc;
	}

	public void setFl(int fl) {
		this.fl = fl;
	}

	public void setOfd(int ofd) {
		this.ofd = ofd;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public void setHl(int hl) {
		this.hl = hl;
	}

	public void setEfw(double efw) {
		this.efw = efw;
	}
	
	public boolean isObgyn() {
		return obgyn;
	}
	
	public void setObgyn() throws DBException {
		this.obgyn = obc.getObGyn();
	}

	/** Image upload **/
	
	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public boolean getIsFileUploaded() {
		return isFileUploaded;
	}

	public void setIsFileUploaded(boolean isFileUploaded) {
		this.isFileUploaded = isFileUploaded;
	}
}


